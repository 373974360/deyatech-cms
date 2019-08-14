package com.deyatech.station.index;

import java.util.Map;

/**
 * 数据索引管理接口
 * @Author csm
 * @Date 2019/08/13
 */
public interface IndexService {

    /**
     * 索引中查询数据
     *
     * @param indexes 索引,多个使用逗号分隔
     * @param query   查询条件（ES搜索语法）
     * @return
     */
    String selectDataByESQueryJSON(String indexes, String query);

    /**
     * 搜索数据，在标题和指定字段中搜索
     *
     * @param indexes    索引,多个使用逗号分隔
     * @param keywords 关键字
     * @return
     */
    String searchData(String indexes, String keywords, String fields);

    /**
     * 根据 id 返回内容
     *
     * @param index 索引 id
     * @param id    数据 id
     * @return
     */
    String getDataById(String index, String id);

    /**
     * 创建/重新创建数据集对应的索引，后台会根据数据集中的元数据创建 mapping，如果索引被覆盖（重建），则索引中的数据需要重建
     *
     * @param index      索引id
     * @param override   是否覆盖旧索引，默认值为 false
     * @param indexAlias
     * @return
     */
    String createIndex(String index, Boolean override, String indexAlias
            , String mcId);

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    String deleteIndex(String index);

    /**
     * 将数据集中的数据重新添加到索引中
     *
     * @param index
     * @return
     */
//    String addAllData(String index);

    /**
     * 删除索引中的所有数据
     *
     * @param indexes
     * @return
     */
//    String delAllData(String indexes);

    /**
     * 向索引中添加数据
     *
     * @param index
     * @param data
     * @return
     */
    String addData(String index, String id, Map data);

    /**
     * 修改索引中的数据
     *
     * @param index
     * @param data
     * @return
     */
    String updateData(String index, String id, Map data);

    /**
     * 删除索引中的数据
     *
     * @param index
     * @param id
     * @return
     */
    String deleteData(String index, String id);

    /**
     * 删除索引中满足条件的数据
     *
     * @param indexes
     * @param query
     * @return
     */
//    String deleteDataByQuery(String indexes, String query);
}
