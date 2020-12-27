# ExcelReader
<p> 

[简体中文](https://github.com/obiscr/ExcelReader/blob/main/README.md)  |
[English](https://github.com/obiscr/ExcelReader/blob/main/README_EN.md)

</p>
ExcelReader是一款ide插件。可以在JetBrains的部分编辑器中解析
<b>xls，xlsx，csv</b> 三种格式的文件，并且支持简单的过滤功能。

![er_example](https://obiscr.oss-cn-hongkong.aliyuncs.com/res/ExcelReader/docs/er_example.png)
![er_light](https://obiscr.oss-cn-hongkong.aliyuncs.com/res/ExcelReader/docs/er_light.png)
![er_dark](https://obiscr.oss-cn-hongkong.aliyuncs.com/res/ExcelReader/docs/er_dark.png)

# 安装
### 在IDE里面进行安装
打开 "File" - “Settings” - “Plugins”，选择 “Marketplace”，输入 `ExcelReader` ，点击 `install` 即可安装到IDE。
> 不同版本的IDE，上述的打开方式可能略有差别

### 通过 WEB 安装
[点击前往ExcelReader插件主页](https://plugins.jetbrains.com/plugin/14722-excelreader)

# 使用
选中一个后缀是 `.xls` 或 `.xlsx` 或 `.csv` 的文件，鼠标右击选择最上面 `Open As ExcelReader` 即可打开。
ExcelReader 同时支持快捷键打开，选中文件后，按下对应的快捷键即可打开。不同的平台快捷键略有差异。

> 在 Windows 或者 Linux 平台
+ Ctrl Shift D

> 在 macOS 平台
+ Command Shift D

打开一个文件之后，ExcelReader界面最上面可以进行简单的搜索操作。

选择 `All Column`，是在所有列中搜索；选择某个具体的列，则只在选择的列中进行搜索。