package com.deyatech.station.index.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.deyatech.common.exception.BusinessException;
import com.deyatech.station.config.SiteProperties;
import com.deyatech.station.index.IndexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.elasticsearch.common.xcontent.DeprecationHandler.THROW_UNSUPPORTED_OPERATION;

/**
 * 数据索引管理接口实现类
 * @Author csm
 * @Date 2019/08/13
 */
@Component
@Slf4j
public class IndexServiceImpl implements IndexService {

    private final String indexType = "_doc";
    private RestHighLevelClient client = null;
    private RestClient restClient = null;
    private ObjectMapper objectMapper = new ObjectMapper();

    public IndexServiceImpl(@Autowired SiteProperties siteProperties) {
        log.info("siteProperties: " + siteProperties);
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(siteProperties.getElasticSearchHostname(), siteProperties.getElasticSearchPort(), "http")
        );
        client = new RestHighLevelClient(restClientBuilder);
        restClient = restClientBuilder.build();
    }

    /**
     * 索引中查询数据
     *
     * @param indexes 索引,多个使用逗号分隔
     * @param query   查询条件（ES搜索语法）
     * @return
     */
    @Override
    public String selectDataByESQueryJSON(String indexes, String query) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
        try {
            XContentParser parser = XContentFactory.xContent(XContentType.JSON).createParser(
                    new NamedXContentRegistry(searchModule.getNamedXContents()), THROW_UNSUPPORTED_OPERATION, query
            );
            searchSourceBuilder.parseXContent(parser);
        } catch (IOException e) {
            log.error("IO错误:" + e.getMessage(), e);
        }
        try {
            SearchRequest searchRequest = Requests.searchRequest(indexes.split(",")).source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            RestStatus status = searchResponse.status();
            if (status == RestStatus.OK) {
                return searchResponse.toString();
            } else {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "发生错误");
            }
        } catch (IOException e) {
            log.error("IO错误:" + e.getMessage(), e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "发生错误");
        }
    }

    /**
     * 搜索数据，在标题和指定字段中搜索
     *
     * @param indexes    索引,多个使用逗号分隔
     * @param keywords 关键字
     * @return
     */
    @Override
    public String searchData(String indexes, String keywords, String fields) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords);

        multiMatchQueryBuilder.field("title", 2f);
        if (StrUtil.isNotEmpty(fields)) {
            for (String f : fields.split(",")) {
                multiMatchQueryBuilder.field(f);
            }
        }
        searchSourceBuilder.query(multiMatchQueryBuilder);

        String s = searchSourceBuilder.toString();
        String stringWSResult = this.selectDataByESQueryJSON(indexes, s);
        return stringWSResult;
    }

    /**
     * 根据 id 返回内容
     *
     * @param index 索引 id
     * @param id    数据 id
     * @return
     */
    @Override
    public String getDataById(String index, String id) {
        GetRequest getRequest = new GetRequest(index, indexType, id);
        // 配置是否返回source
        getRequest.fetchSourceContext(FetchSourceContext.FETCH_SOURCE);
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            return getResponse.toString();
        } catch (IOException e) {
            log.error("查询失败:" + e.getMessage(), e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "查询失败");
        }
    }

    /**
     * 创建/重新创建数据集对应的索引，后台会根据数据集中的元数据创建 mapping，如果索引被覆盖（重建），则索引中的数据需要重建
     *
     * @param index      索引id
     * @param override   是否覆盖旧索引，默认值为 false
     * @param indexAlias
     * @return
     */
    @Override
    public String createIndex(String index, Boolean override, String indexAlias, String mcId) {

        // 获取元数据集 TODO
//        MetaDataCollectionVo metaDataCollectionVo = this.getMetaDataCollectionById(mcId);
//        if (metaDataCollectionVo != null) {
            try {
                boolean indexExists;

                GetIndexRequest getIndexRequest = new GetIndexRequest();
                getIndexRequest.indices(index);
                getIndexRequest.includeDefaults(true);
                try {
                    indexExists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    log.error(String.format("判断索引是否存在失败 %s", index), e);
                    throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("判断索引是否存在失败 %s", index));
                }

                XContentBuilder source = XContentFactory.jsonBuilder();
                if (indexExists) {
                    if (!override) {
                        throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("创建索引失败，%s，可以尝试删除后重新建立", index));
                    }
                    //删除旧索引，重新建立新索引
                    this.deleteIndex(index);
                }

                CreateIndexRequest request = Requests.createIndexRequest(index);
                request.settings(Settings.builder()
                        // 分片
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 1)
                        // 拼音分词
                        .put("index.analysis.analyzer.ik_pinyin_analyzer.type", "custom")
                        .put("index.analysis.analyzer.ik_pinyin_analyzer.tokenizer", "ik_smart")
                        .put("index.analysis.analyzer.ik_pinyin_analyzer.filter", "pinyin_filter")
                        .put("index.analysis.filter.pinyin_filter.type", "pinyin")
                        .put("index.analysis.filter.pinyin_filter.keep_separate_first_letter", false)
                        .put("index.analysis.filter.pinyin_filter.keep_full_pinyin", true)
                        .put("index.analysis.filter.pinyin_filter.keep_original", true)
                        .put("index.analysis.filter.pinyin_filter.limit_first_letter_length", 16)
                        .put("index.analysis.filter.pinyin_filter.lowercase", true)
                        .put("index.analysis.filter.pinyin_filter.remove_duplicated_term", true)
                );
                source.startObject().startObject(indexType);
                // 类型映射 TODO
//                this.typeMapping(source, metaDataCollectionVo, index);
                source.endObject().endObject();
                request.mapping(indexType, source);
                CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
                if (createIndexResponse.isShardsAcknowledged()) {
                    if (StrUtil.isNotBlank(indexAlias)) {
                        IndicesAliasesRequest aliasesRequest = new IndicesAliasesRequest();
                        aliasesRequest.addAliasAction(new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD).alias(indexAlias).index(index));
                        IndicesAliasesResponse indicesAliasesResponse = client.indices().updateAliases(aliasesRequest, RequestOptions.DEFAULT);
                        if (indicesAliasesResponse.isAcknowledged()) {
                            log.info(String.format("索引 %s 的别名 %s创建成功", index, indexAlias));
                        }
                    }
                    return String.format("创建索引成功,%s", index);
                } else {
                    throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("创建索引失败，%s", index));
                }

            } catch (IOException e) {
                log.error(String.format("创建或更新失败 %s", e.getMessage()), e);
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("创建或更新失败，%s", index));
            }
//        } else {
//            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("创建索引失败，查询元数据集错误，元数据集 id =  %s", mcId));
//        }

    }

    /**
     * 根据id获取元数据集
     * @param mcId 元数据集id
     */
/*    private MetaDataCollectionVo getMetaDataCollectionById(String mcId) {
        return siteCache.getMetaDataCollectionById(mcId);
    }*/

    /**
     * 类型映射
     * @param source
     * @param metaDataCollectionDto
     * @param indexName
     * @throws IOException
     */
/*    private void typeMapping(XContentBuilder source, MetaDataCollectionVo metaDataCollectionVo, String indexName) throws
            IOException {
        List<MetaDataCollectionMetaDataDto> metaDataCollectionMetaDataDtoList = metaDataCollectionDto.getMetaDataCollectionMetaDataDtoList();
        if (metaDataCollectionMetaDataDtoList != null && !metaDataCollectionMetaDataDtoList.isEmpty()) {
            XContentBuilder properties = source.startObject("properties");
            for (MetaDataCollectionMetaDataDto metaDataCollectionMetaDataDto : metaDataCollectionMetaDataDtoList) {
                MetaDataDto metaData = metaDataCollectionMetaDataDto.getMetaData();
                //TODO  field 名称 保持与生成的 entity 一致的做法（entity生成时目前使用前缀拼接英文名作为属性名，此处保持一致）
                String value = this.convertDataType(metaData.getDataType());
                XContentBuilder field = properties.startObject(metaDataCollectionDto.getMdPrefix() + metaData.getBriefName())
                        .field("type", value);
                //如果是 text并且使用全文检索，则使用 ik 分词
                if ("text".equalsIgnoreCase(value) && metaDataCollectionMetaDataDto.getUseFullIndex() != null && metaDataCollectionMetaDataDto.getUseFullIndex() == 1) {
                    field.field("analyzer", "ik_pinyin_analyzer");
                    field.field("search_analyzer", "ik_pinyin_analyzer");
                } else if ("date".equalsIgnoreCase(value)) { //如果是日期则，使用标准的日期格式
                    field.field("format", "yyyy-MM-dd HH:mm:ss");
                }
                field.endObject();

            }

            //通用字段定义: 站点id
            XContentBuilder siteId = properties.startObject("siteId");
            siteId.field("type", "keyword");
            siteId.endObject();

            //通用字段定义：栏目id
            XContentBuilder cmsCatalogId = properties.startObject("cmsCatalogId");
            cmsCatalogId.field("type", "keyword");
            cmsCatalogId.endObject();

            //通用字段定义：内容表记录id
            XContentBuilder contentId = properties.startObject("contentId");
            contentId.field("type", "keyword");
            contentId.endObject();

            XContentBuilder status = properties.startObject("status");
            status.field("type", "integer");
            status.endObject();

            //通用字段定义: 标题
            XContentBuilder title = properties.startObject("title");
            title.field("type", "text");
//            title.field("analyzer", "ik_max_word");
            title.field("analyzer", "ik_pinyin_analyzer");
            title.field("search_analyzer", "ik_pinyin_analyzer");
            title.endObject();

            //通用字段定义: 缩略图
            XContentBuilder thumbnail = properties.startObject("thumbnail");
            thumbnail.field("type", "keyword");
            thumbnail.endObject();

            //通用字段定义: 作者
            XContentBuilder author = properties.startObject("author");
            author.field("type", "keyword");
            author.endObject();

            //通用字段定义: 来源
            XContentBuilder dataSource = properties.startObject("source");
            dataSource.field("type", "keyword");
            dataSource.endObject();

            //通用字段定义: 小编
            XContentBuilder editor = properties.startObject("editor");
            editor.field("type", "keyword");
            editor.endObject();

            //通用字段定义: URL
            XContentBuilder url = properties.startObject("url");
            url.field("type", "keyword");
            url.endObject();

            //通用字段定义: 创建时间
            XContentBuilder createTime = properties.startObject("createTime");
            createTime.field("type", "date");
//            createTime.field("format", "yyyy-MM-dd HH:mm:ss");
            createTime.endObject();

            //通用字段定义: 更新时间
            XContentBuilder updateTime = properties.startObject("updateTime");
            updateTime.field("type", "date");
//            updateTime.field("format", "yyyy-MM-dd HH:mm:ss");
            updateTime.endObject();

            XContentBuilder content = properties.startObject("content");
            content.field("type", "object");
            content.endObject();

            properties.endObject();

        } else {
            logger.error(String.format("创建索引失败，数据集 %s 中没有有效属性", indexName));
        }

    }*/

    /**
     * 转换 ecp 的元数据类型为 elasticsearch 支持的数据类型
     *
     * @param dataType
     * @return
     */
    private String convertDataType(String dataType) {
        switch (dataType) {
            case "string":
                return "text";
            case "int":
                return "integer";
            case "long":
                return "long";
            case "float":
                return "float";
            case "double":
                return "double";
            case "boolean":
                return "boolean";
            case "text":
                return "text";
            case "datetime":
                return "date";
            case "date":
                return "date";
            default:
                return "keyword";
        }
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    @Override
    public String deleteIndex(String index) {

        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            request.timeout(TimeValue.MINUS_ONE);
            request.indicesOptions(IndicesOptions.lenientExpandOpen());
            DeleteIndexResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
            if (deleteIndexResponse.isAcknowledged()) {
                return String.format("删除索引成功，%s", index);
            } else {
                return String.format("删除索引失败 %s", index);
            }
        } catch (IOException e) {
            log.error(String.format("创建索引失败,%s", index), e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("创建索引失败 %s", index));
        }
    }

    /**
     * 向索引中添加数据
     *
     * @param index
     * @param data
     * @return
     */
    @Override
    public String addData(String index, String id, Map data) {

        IndexRequest request = new IndexRequest(index, indexType);
        // 同步调用
        try {
            IndexRequest source = request.source(data);
            // 如果给定了 id 则使用给定的 id
            if (StrUtil.isNotBlank(id)) {
                source.id(id);
            }
            IndexResponse response = client.index(source, RequestOptions.DEFAULT);
            if (response.status() == RestStatus.CREATED) {
                return String.format("添加索引数据成功,%s", index);
            } else {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("添加索引数据失败,%s", index));
            }
        } catch (IOException e) {
            log.error(String.format("添加索引数据失败,%s,%s", index, data), e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("添加索引数据失败,%s", index));
        }
    }

    /**
     * 修改索引中的数据
     *
     * @param index
     * @param data
     * @return
     */
    @Override
    public String updateData(String index, String id, Map data) {

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, "修改索引数据失败,id 不能为空");
        }
        UpdateRequest updateRequest = new UpdateRequest(index, indexType, id);
        //同步调用
        try {
            UpdateResponse response = client.update(updateRequest.doc(data), RequestOptions.DEFAULT);
            if (response.status() == RestStatus.OK) {
                return String.format("修改索引数据成功,%s下的%s", index, id);
            } else {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("修改索引数据失败,%s下的%s", index, id));
            }
        } catch (IOException e) {
            log.error(String.format("修改索引数据失败 %s下的%s,%s", index, id, data), e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("修改索引数据失败,%s下的%s", index, id));
        }
    }

    /**
     * 删除索引中的数据
     *
     * @param index
     * @param id
     * @return
     */
    @Override
    public String deleteData(String index, String id) {

        DeleteRequest deleteResponse = new DeleteRequest(index, indexType, id);
        //同步调用
        try {
            DeleteResponse response = client.delete(deleteResponse, RequestOptions.DEFAULT);
            if (response.getResult() == DocWriteResponse.Result.DELETED) {
                return String.format("删除索引数据成功,%s下的%s", index, id);
            } else if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.info(String.format("删除索引数据 %s下的%s不存在", index, id));
                return String.format("删除索引数据成功,%s下的%s", index, id);
            } else {
                throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("删除索引数据失败,%s下的%s,返回%s", index, id, response.toString()));
            }
        } catch (IOException e) {
            log.error(String.format("删除索引数据失败,%s下的%s", index, id), e);
            throw new BusinessException(HttpStatus.HTTP_INTERNAL_ERROR, String.format("删除索引数据失败,%s下的%s", index, id));
        }
    }


}
