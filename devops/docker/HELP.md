#自动安装docker
curl -sSL https://get.daocloud.io/docker | sh
#阿里云加速自动安装docker
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
#启动docker
service docker start
#设置国内加速
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
      "https://nwj24vy0.mirror.aliyuncs.com",
      "https://08d329662300f38d0f27c012d28caf60.mirror.swr.myhuaweicloud.com"
    ],
    "insecure-registries": [
     "192.168.0.227:5000"
    ]
}
EOF
systemctl daemon-reload
systemctl restart docker
#开放2375端口 不建议使用
vim /usr/lib/systemd/system/docker.service
##ExecStart修改启动参数 
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock
systemctl daemon-reload
systemctl restart docker
#容器可视化
docker pull docker.io/portainer/portainer
#启动可视化
docker run -d -p 9000:9000 --restart=always -v /var/run/docker.sock:/var/run/docker.sock --name portainer portainer/portainer 
#Docker Compose安装
yum install epel-release
yum -y install docker-compose
#Docker 配置证书
运行create_tls_certs.sh
##ExecStart修改启动参数
ExecStart=/usr/bin/dockerd --tlsverify --tlscacert=/etc/docker/ca.pem --tlscert=/etc/docker/server-cert.pem --tlskey=/etc/docker/server-key.pem -H tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock
systemctl daemon-reload
systemctl restart docker