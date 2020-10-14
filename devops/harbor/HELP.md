#Harbor安装
cd /opt
wget https://storage.googleapis.com/harbor-releases/release-1.10.0/harbor-offline-installer-v1.10.4.tgz
tar xf harbor-offline-installer-v1.10.4.tgz 
cd harbor/
#修改harbor.yml文件 修改下hostname为本机的ip，harbor_admin_password web页面的密码
./prepare
sh install.sh 
#添加私库认证(非https私库必须添加)
vim /etc/docker/daemon.json
{
  "registry-mirrors": [
    "https://nwj24vy0.mirror.aliyuncs.com",
    "https://08d329662300f38d0f27c012d28caf60.mirror.swr.myhuaweicloud.com"
  ],
  "insecure-registries": [
   "192.168.0.227:5000"
  ]
}
systemctl daemon-reload
systemctl restart docker
#启动Harbor
docker-compose up -d
#停止Harbor
docker-compose stop
#停止并删除容器
docker-compose down
#重启Harbor
docker-compose restart