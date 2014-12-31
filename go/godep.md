# 安装godep

官方的安装文档是使用`go get github.com/tools/godep`,很可惜，因为“网络”问题会报一个找不到`golang.org/x/tools/go/vcs`的错误。

而https://github.com/golang/tools是golang.org/x/tools的一个镜像，代码是一样的，所以我是如下安装的。

```
go get github.com/golang/tools
```

在`GOPATH\src\github.com`目录下就有`tools`文件夹。

在`src`下和`github.com`平级新建`golang.org`文件下，在此文件夹下建`x`文件夹，然后将`tools`都复制进去。

然后再执行`go get github.com/tools/godep`。

此时godep安装在你的`GOPATH\bin`目录下。


# 使用godep

建立一个演示项目来演示godep。

- 此演示项目的路径要加入到GOPATH

- 依赖的项目和项目本身都应该是个git repository

 - `cd skeleton\src\wiselyman.org\app` 
  
    ```
	git init 
	git add .
	git commit
    ```
 - `cd skeleton\src\xx.org\dep` 
  
    ```
	git init
	git add .   
	git commit   
    ```
- 项目目录及代码如图所示    

![](https://raw.githubusercontent.com/wiselyman/study/master/go/resources/godep-main.jpg)
![](https://raw.githubusercontent.com/wiselyman/study/master/go/resources/godep-external.jpg)



- 在`skeleton\src\wiselyman.org\app`目录下，执行`godep save`,此时会生成Godeps文件夹

![](https://raw.githubusercontent.com/wiselyman/study/master/go/resources/godep-Godeps.jpg)





