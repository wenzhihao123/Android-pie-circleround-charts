# Android-pie-circleround-charts
最近在研究绘图方面的内容，很多应用都或多或少的涉及到图表，来展示数据。打开支付宝、记账本等等 ，都有用到饼图。
所以最近就自己动手画了一个圆环图，每个圆环部分表示对映相所占总数 的比例，下面有图例来表示，如图：


![](http://p1.bqimg.com/4851/60f1603a52d543ee.jpg)

使用起来也很简单：
```
<com.wzh.charts.view.CircleRoundView
        android:id="@+id/circleRound"
        android:layout_width="match_parent"
        android:paddingLeft="20dp"
        android:paddingRight="20dp"
        android:layout_height="match_parent"
        app:mtitle="总营业额148万"
        app:mtitleTextColor="@color/font_color1"
        app:mtitleTextSize="@dimen/font_big"
        app:stitle="各年所占比例"
        app:stitleTextColor="@color/colorAccent"
        app:stitleTextSize="@dimen/font_smalll"
        app:centerBackgroundColor="@color/white"
        app:legendColumn="3"
        app:legendTextColor="@color/font_color1"
        app:legendTextSize="@dimen/font_smalll"
        app:isRotate="true"
        />


- mtitle:代表圆环内部大标题
- stitle：代表圆环内部小标题
- centerBackgroundColor：中心圈的背景颜色
- legendColumn：图例每行显示的个数
- isRotate: 点击的时候是否旋转

```
在代码里面设置内容数据，这里暴漏的方法不全，可以自己去扩展：
```
private String[] lable = {"2012","2013", "2014","2015","2016","2017"};
private int[] colors = {Color.parseColor("#ff0033"),Color.parseColor("#99ffff"),
                        Color.parseColor("#66ff33"),Color.parseColor("#FFEC8B"),
                        Color.parseColor("#FF83FA"),Color.parseColor("#AEEEEE"),
                        Color.parseColor("#912CEE"),Color.parseColor("#595959"),
                        Color.parseColor("#AEEEEE"),Color.parseColor("#912CEE")};

//设置圆环的颜色
circleRoundView.setColors(colors);
//设置int型数据
circleRoundView.setData(data);
//设置图例上的lable
circleRoundView.setLable(lable);
//设置图例显示个数
circleRoundView.setLegendColumn(3);

```
绘制出来之后，可以点击相应的部分，会跟其他部分分离开，起到凸显作用。
绘制该view主要就是练习绘制的过程及api，熟悉canvas.save()及canvas.restore()方法，canvas动画的执行放在两者之间，canvas.rotate(),防止中心圆及其他区域会跟着旋转。
有这方面需求的可以做个参考，希望可以帮到你~