package com.deyatech.template.utils;

import com.deyatech.common.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

/**
 * @Description: JGit
 * @Author: zhaichen
 * @Date: 2018-07-16 14:11
 * @Version: 1.0
 * @Created in idea by autoCode
 */
public class JGitUtil {

    private static Logger logger = LoggerFactory.getLogger(JGitUtil.class);

    /**
     * 公共资源文件存放路径
     */
    @Value("${site.domain.rootDir}")
    private static String LOCALPATH; //下载已有仓库到本地路径

    /**
     * 本地仓库新增文件
     */
    public static void testAdd() throws IOException, GitAPIException {
        File myfile = new File(LOCALPATH + "/myfile.txt");
        myfile.createNewFile();
        //git仓库地址
        Git git = new Git(new FileRepository(LOCALPATH+"/.git"));

        //添加文件
        git.add().addFilepattern("myfile").call();
    }

    /**
     * 本地提交代码
     */
    public static void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        //git仓库地址
        Git git = new Git(new FileRepository(LOCALPATH+"/.git"));
        //提交代码
        git.commit().setMessage("test jGit").call();
    }


    /**
     * 拉取远程仓库内容到本地
     */
    public static void pull() throws IOException, GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("username","password");
        //git仓库地址
        Git git = new Git(new FileRepository(LOCALPATH+"/.git"));
        git.pull().setRemoteBranchName("master").
                setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    /**
     * push本地代码到远程仓库地址
     */
    public static void testPush() throws IOException, JGitInternalException,
            GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("username","password");
        //git仓库地址
        Git git = new Git(new FileRepository(LOCALPATH+"/.git"));
        git.push().setRemote("origin").     setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    /**
     * @param dir 远程地址
     */
    public static void gitPull(String dir) {
        File RepoGitDir = new File(dir);
        if (!RepoGitDir.exists()) {
            logger.info("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
        } else {
            Repository repo = null;
            try {
                repo = new FileRepository(RepoGitDir.getAbsolutePath());
                Git git = new Git(repo);
                PullCommand pullCmd = git.pull().setRemoteBranchName("master");
                pullCmd.call();

                logger.info("Pulled from remote repository to local repository at " + repo.getDirectory());
            } catch (Exception e) {
                logger.info(e.getMessage() + " : " + RepoGitDir.getAbsolutePath());
            } finally {
                if (repo != null) {
                    repo.close();
                }
            }
        }
    }

    /**
     * 克隆远程库
     * @param remoteUrl 远程地址
     * @param dir 存放路径
     * @param userName git username
     * @param passWord git password
     * @throws IOException
     * @throws GitAPIException
     */
    public static void cloneRepository(String remoteUrl, String dir, String userName, String passWord) throws IOException, GitAPIException {

        File localPath1 = new File(dir);

        File gitFile = new File(dir + "/.git");

        if (gitFile.exists()) {
            try {
                Git gitResult = Git.open(localPath1);
                gitResult.checkout().setCreateBranch(false).setName("master").setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).call();
                gitResult.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, passWord)).call();

            } catch (Exception e) {
                logger.info(e.getMessage() + " : " + localPath1.getAbsolutePath());
                if ("not authorized".equals(e.getMessage())) {
                    throw new BusinessException(403,"用户名或密码错误");
                } else {
                    e.printStackTrace();
                    throw new BusinessException(500,"未知错误");
                }
            }
        } else {
            // clean up here to not keep using more and more disk-space for these samples
            FileUtils.deleteDirectory(localPath1);

            // then clone
            System.out.println("Cloning from " + remoteUrl + " to " + localPath1);
            try (Git result = Git.cloneRepository()
                    .setBranch("master")
                    .setURI(remoteUrl)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider( userName, passWord ))
                    .setDirectory(localPath1)
                    .call()) {
                // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
                System.out.println("Having repository: " + result.getRepository().getDirectory());
            }
        }
    }

    public static void main(String[] args) throws IOException, GitAPIException {

    }

}
