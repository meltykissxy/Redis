# Redis
关于Jedis的使用等基础操作

## Redis安装

Redis是C语言开发的，安装跟Java完全不一样，烦死我了

1. 装gcc
sudo yum install centos-release-scl scl-utils-build 
sudo yum install -y devtoolset-8-toolchain
sudo scl enable devtoolset-8 bash
注意scl命令后，此时账号显示变为root了，继续操作
sudo gcc --version（检查版本是否正确）

2. 安装Redis
上面scl切换到root用户了哦，继续在root用户下面操作
  1. 解压命令：tar -zxvf redis-6.0.8.tar.gz
  2. 解压完成后进入目录：cd redis-6.0.8
  3. 在redis-6.0.8目录下执行make命令
  4. 然后执行make install

安装完了哦，在/usr/local/bin能看到6个文件。以后你想删Redis时，直接把这6个文件删了就行，so easy。但是别tm进去，别给自己找麻烦

注意注意注意注意：关闭Terminal，重新进入自己的账号（刚才安装其实都是在root下操作的，你没注意不是你的错，是scl命令自动给你拽过去的）

3. 配置
在我们tar解压后的文件夹内的redis.conf复制一份到任何位置都行
cp  /opt/redis-6.0.8/redis.conf ~/

我们去改这个复制出来的文件
vim ./redis.conf
在第225行左右，把daemonize no改为daemonize yes
第260行，指定存放日志的地址，例如logfile "/opt/software/redislog.log"
第365行，把dir ./改成dir /opt/software（Redis在任何位置都能启动，但同时会在启动地址创建一个dump.rdb文件，这个配置就是让它老实点儿）

4. 千万别切到/usr/local/bin启动，打死你！！！

**随便找个地方启动，别去那里启动，启动命令全局都能用**

现在就可以启动了哦：redis-server ./redis.conf
启动后没反应？对哦，其实已经启动了。想知道怎么看嘛？
ps -ef|grep redis，看进程（C语言开发的，所以jps看不到）

5. 关闭服务器端
redis-cli shutdown

6. 使用
redis-cli进入命令行
（完整是redis-cli -p 6379 -h 127.0.0.1），你也可以进入其他服务器，把ip地址改一下就行，但是对方的配置需要改，还要设密码，详情见MindMap

测试：
set k1 v1
get k1
或者ping

select 0-15（16个数据库，没名，就是数字，select 0、select 1到select 15，生产中基本用select 0，不切库）


问题：
shutdown关不掉：是不是自己切到/usr/local/bin才启动的？
可别这样子。redis-server全局都能用。你在bin目录下启动，结束时要求你备份，但是备份是写进当前文件夹中的，所以就卡死了。
要是踩了这个坑，只能kill了：
ps -ef|grep redis，找到进程号
kill -9 进程号

