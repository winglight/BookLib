BookLib
=======

## A personal/small book library tool, SCAN almost could make everything done

## 面向个人或公司组织的小型图书馆管理工具，通过扫描书本条码进行登记、借阅、归还等功能，目前功能有限，以后将继续完善增加以下功能：

* 1.排序：可以按照公司书号排序、图书名字排序
* 2.数据导出：可以直接筛选或者导出借书人员名单（或者说不在库图书的清单和借阅人、借阅时间）
* 3.数据备份：云备份，这样手机坏了或者重新安装也可以保证数据不丢失
* 4.系统管理员：只有系统管理员有权限进行借阅管理，其他人可以拿此手机查询图书
* 5.书籍查询：大家用此手机进行名称、分类等查询，增加分类字段，如：安卓书籍，ios开发书籍等等

## 使用说明：

* 1.因为使用了zxing作为扫描条码工具，所以如果手机上没有安装zxing，将会提示下载安装
* 2.同样一本书暂时不可以通过扫描增加多本，因为当前的业务逻辑是：如果数据库中已经存在书籍A，那么再次扫描到这本书将自动打开详情，进行借阅或归还
* 3.书籍的状态是：在库、已借
* 4.书籍类别和借书人的录入框带有自动完成功能，点击将弹出历史输入记录

## Bug Report:

* [issues](issues)
* E-mail: vncntkarl2 At gmail.com