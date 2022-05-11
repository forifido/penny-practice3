# README
## dependence
1. java >=8
2. maven
## build
```shell
cd <project_root>
mvn clean package
```
## execute
```shell
java -jar target/penny-practice3-1.0-SNAPSHOT.jar "这个老板不怎么骗人，但是一次上门体验的经历还是让我非常后悔。" "这块蛋糕没有异味。" "我非常地放心这一个没有出过问题的电吹风"
```
> 不.骗人 -1;非常.后悔 5
> 
> 没.异味 -1
> 
> 没.问题 -1;非常.放心 -1.5

