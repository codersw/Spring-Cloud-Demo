#启动镜像
docker-compose up -d
#停止镜像
docker-compose stop
#停止并删除
docker-compose down
#重启
docker-compose restart
#创建本地映射目录
mkdir -p /root/jenkins
sudo chown -R 1000 /root/jenkins
#查看初始化秘钥
cat /root/jenkins/secrets/initialAdminPassword
##或者进入容器
docker exec -u 0 -it jenkins bash
cat /var/jenkins_home/secrets/initialAdminPassword
#配置git maven jdk
##Maven 配置
wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
tar zvxf apache-maven-3.6.3-bin.tar.gz
cd  /apache-maven-3.6.3/conf/
vi  /settings.xml
<mirrors>
    <mirror>
        <id>alimaven</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
<pluginGroups>
    <pluginGroup>io.fabric8</pluginGroup>
    <pluginGroup>org.springframework.boot</pluginGroup>
</pluginGroups>
重命名文件夹 maven
环境变量
echo "export PATH=$PATH:/usr/local/maven/bin" >> /etc/bashrc
立即生效
source /etc/bashrc
取宿主机目录映射
/usr/local/maven/conf/settings.xml
##Git 配置
docker容器默认
/usr/bin/git
##Jdk 配置
docker容器默认
/usr/local/openjdk-8
##Nodejs 配置
wget https://nodejs.org/dist/v12.14.0/node-v12.14.0-linux-x64.tar.gz
tar xf node-v14.2.0-linux-x64.tar.xz
重命名文件夹 nodejs
环境变量
echo "export PATH=$PATH:/usr/local/nodejs/bin" >> /etc/bashrc
立即生效
source /etc/bashrc
取宿主机目录映射
/usr/local/node
#需要安装的插件
git gitlab maven ssh docker nodejs
#邮件设置
![avatar](1.png)
![avatar](2.png)
![avatar](3.png)
![avatar](4.png)
![avatar](5.png)
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${ENV, var="JOB_NAME"}-第${BUILD_NUMBER}次构建日志</title>
</head>

<body leftmargin="8" marginwidth="0" topmargin="8" marginheight="4"
    offset="0">
    <table width="95%" cellpadding="0" cellspacing="0"
        style="font-size: 11pt; font-family: Tahoma, Arial, Helvetica, sans-serif">
        <tr>
            <td><br />
            <b><font color="#0B610B">构建信息</font></b>
            <hr size="2" width="100%" align="center" /></td>
        </tr>
        <tr>
            <td>
                <ul>
                    <li>项目名称 ： ${PROJECT_NAME}</li>
                    <li>构建编号 ： 第${BUILD_NUMBER}次构建</li>
                    <li>GIT 版本： ${GIT_REVISION}</li>
                    <li>触发原因： ${CAUSE}</li>
                    <li>构建日志： <a href="${BUILD_URL}console">${BUILD_URL}console</a></li>
                    <li>构建  Url ： <a href="${BUILD_URL}">${BUILD_URL}</a></li>
                    <li>工作目录 ： <a href="${PROJECT_URL}ws">${PROJECT_URL}ws</a></li>
                    <li>项目  Url ： <a href="${PROJECT_URL}">${PROJECT_URL}</a></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td><b><font color="#0B610B">变更集</font></b>
            <hr size="2" width="100%" align="center" /></td>
        </tr>

        <tr>
            <td>${JELLY_SCRIPT,template="html"}<br/>
            <hr size="2" width="100%" align="center" /></td>
        </tr>


    </table>
</body>
</html>
