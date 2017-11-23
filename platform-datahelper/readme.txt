模块名称:platform-datahelper
父模块:platform-parent
子模块:platform-datahelper-cdhhandler
      platform-datahelper-databasehandler
      platform-datahelper-grabhandler
      platform-datahelper-datapacker

打包:pom

功能:数据处理模块父类,用于集中定义数据处理通用的依赖,作用类似于工具,需要的时候直接依赖子类
    处理包括但不限于:标签-文本的统一形式(LabeldSentence)
                  标签-图像
                  文本群....等等(不依赖与deeplearning4j,为dl4j和其他服务提供基础
                  数据形式的支持)

ps:打包好后最终以序列化的文件形式存放