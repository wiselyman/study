# cd
 /etc/profile
alias cd1="cd .."
alias cd2="cd ../.."
alias cd3="cd ../../.."
alias cd4="cd ../../../.."
alias cd5="cd ../../../../.."
alias cd6="cd ../../../../../.."

function mkdircd () { 
    mkdir -p "$@" && eval cd "\"\$$#\"";
}

cd - 两个文件夹切换


shopt -s cdspell 纠正拼写错误


# grep
grep John /etc/passwd 显示匹配的
grep -v John /etc/passwd 显示不匹配的
grep -c John /etc/passwd 多少行匹配
grep -cv John /etc/passwd 多少行不匹配
grep -i john /etc/passwd 不区分大小写ignore
grep -r john /home/users 子目录查找

# find
find /etc -name "*mail*"
find / -type f -size +100M 显示大于100M的文件

# join 
join employee.txt bonus.txt

tr a-z A-Z < employee.txt 将文档中的小写全部转换为大写

stat /etc/my.cnf 显示文件或文件夹信息

stat -f / 显示文件系统状态

# zip
zip var-log-files.zip /var/log/*

unzip var-log.zip

tar xvfz /tmp/my_home_directory.tar.gz 解压


ctrl+R搜索command历史

dd if=/dev/zero of=/home/swap-fs bs=1M count=512 创建swap文件


df –h

sar –u

vmstat 1 100
 