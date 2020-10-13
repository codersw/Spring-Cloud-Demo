# 只在 master 节点执行
# export 命令只在当前 shell 会话中有效，开启新的 shell 窗口后，如果要继续安装过程，请重新执行此处的 export 命令
export MASTER_IP=192.168.175.101
# 替换 k8s.master 为 您想要的 dnsName
export APISERVER_NAME=k8s.master
# Kubernetes 容器组所在的网段，该网段安装完成后，由 kubernetes 创建，事先并不存在于物理网络中
export POD_SUBNET=172.18.0.1/16
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
