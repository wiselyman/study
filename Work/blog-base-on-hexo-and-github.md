title: 基于Github制作自己的博客
categories: NonTech
tags: github
---



### GitHub准备

- 注册github账号,并新建一个`用户名+github.io`的repository

### 下载安装nodejs

### 安装hexo

```bash
npm install hexo-cli -g #安装
hexo init blog #初始化项目
cd blog
npm install #安装依赖
hexo server #本地查看效果,访问地址为http://localhost:4000
```

### 更换主题
- 安装配置`freemind`主题
- 在blog目录下
- `git clone https://github.com/wzpan/hexo-theme-freemind.git themes/freemind`
- `npm install hexo-tag-bootstrap --save`
- 添加blog目录下添加的`source`目录下分别添加`categories`,`tags`,`about`,并分别新建index.html
  
  内容分别如下：
  
```javascript
title: Categories
layout: categories
---
```
  
```javascript
title: Tags
layout: tags
---
```
  
```javascript
title: About
layout: page
---

Something about me. ;-)
```

- 配置freemind

在`blog\themes\freemind`目录下有个`_config.yml`请按照需要修改

### 配置hexo
- 在`blog`根目录下也有个`_config.yml`,按照自己需求修改
- 其中主题修改`theme: freemind`


### 发布到github
- 先安装git部署插件

```bash
npm install hexo-deployer-git --save

```

- 修改根目录下`_config.yml`

```javascript
deploy:
  type: git
  repository: https://github.com/wiselyman/wiselyman.github.io.git
  branch: master
```

- 发布

```bash
hexo clean  
hexo generate  
hexo deploy #输入帐号密码
```

