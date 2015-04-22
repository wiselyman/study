RPM包管理是红帽系列的Linux(Redhat,Suse,CentOS,Fedora..)通用的包管理器。


#示例及解释
- rpm -ivh xx.rpm (安装rpm包)
  - -i：install 安装
  - -v：verbose 冗长
  - -h：hash 打印hash


- rpm -qa | grep 'xx' (查询rpm包)
  - -q:query 查询操作
  - -a:all 所有已安装的rpm包


- rpm -q xx （查询特定rpm包）


- rpm -qf /usr/bin/xx (文件属于哪个rpm包)
  - -f: file 文件名


- rpm -qi xx (该rpm包的信息)
  - -i: information 信息


- rpm -qip xx.rpm (指定rpm包文件查看包信息)
  - -p：package 指定包名


- rpm -qpl xx.rpm (查询包里的所有文件)
  - -l: list 列表


- rpm -qRp xx.rpm (显示此包的依赖)


- rpm -e xx (卸载)
  - -e:erase 删除