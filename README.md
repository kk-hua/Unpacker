# Unpacker
通过注解处理器生成TCP/串口数据解析器模板代码。

- 在实体类中的属性上注解 `@KField` 
- 每个实体类必须有且只有一个 `@KId`

#### 支持解析数据类型

> - String
> - int
> - long
> - byte

#### **示例**

```java
//数据实体类
public class Test {

    //cmd指令编号，可以是数据中的某个值，但必须是唯一的，跟其它帧数据不能相同
    //value描述信息，说明Test是什么数据
    @KId(cmd = 1, value = "测试指令") 
    @KField(index = 0)
    private String head;
    
    //index表示在帧数据中的开始下标
    //byteLen表示字节长度，默认是一个byte
    @KField(index = 1, byteLen = 2)
    private int length;
    @KField(index = 3, byteLen = 4)
    private int bi;
    @KField(index = 7,byteLen = 3)
    private long bo;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBi() {
        return bi;
    }

    public void setBi(int bi) {
        this.bi = bi;
    }

    public long getBo() {
        return bo;
    }

    public void setBo(long bo) {
        this.bo = bo;
    }

    @Override
    public String toString() {
        return "Test{" +
                "head='" + head + '\'' +
                ", length=" + length +
                ", bi=" + bi +
                ", bo=" + bo +
                '}';
    }
}
```

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var render: IRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        render = Test_render { cmd, obj ->
            Log.e("MainActivity", "Cmd=$cmd")
            Log.e("MainActivity", "obj=$obj")
        }
    }

    fun test(view: View) {
        render.render(byteArrayOf(0x0, 0x1, 0x2, 0x0, 0x0, 0x5, 0x6, 0x7, 0x8, 0x9))
    }
}

//日志输出：
//MainActivity: Cmd=1
//MainActivity: obj=Test{head='00', length=258, bi=1286, bo=460809}
```

