<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.bigdata.visualanalysis.bean.CategoryAverageRating" %>
<%@page import="com.bigdata.visualanalysis.bean.Discount" %>
<%@page import="com.bigdata.visualanalysis.bean.BrandCount" %>
<%@page import="com.bigdata.visualanalysis.bean.ProductCount" %>
<%@page import="com.bigdata.visualanalysis.bean.OutOfStockCount" %>
<%@page import="com.bigdata.visualanalysis.bean.CategoryCount" %>
<%@page import="com.bigdata.visualanalysis.bean.SubCategoryCount" %>
<%@page import="com.bigdata.visualanalysis.bean.SellerCount" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Flipkart E-commerce on Big Data Dashboard</title>
    <script type="text/javascript" src="js/jquery.js"></script>
    <link rel="stylesheet" href="css/comon0.css">
</head>
<script>
    $(window).load(function(){
        $(".loading").fadeOut()
    })
    /****/
    $(document).ready(function(){
        var whei=$(window).width()
        $("html").css({fontSize:whei/20})
        $(window).resize(function(){
            var whei=$(window).width()
            $("html").css({fontSize:whei/20})
        });
    });


</script>
<script type="text/javascript" src="js/echarts.min.js"></script>
<script type="text/javascript" src="js/china.js"></script>
<script type="text/javascript" src="js/dist_echarts-wordcloud.js"></script>

<body>
<div class="canvas" style="opacity: .2">
    <iframe frameborder="0" src="js/index.html" style="width: 100%; height: 100%"></iframe>
</div>
<div class="loading">
    <div class="loadbox"><img src="picture/loading.gif"> 页面加载中...</div>
</div>
<div class="head">
    <h1>E-commerce data analysis based on big data</h1>
    <div class="weather">
        <!--        <img src="picture/weather.png"><span>多云转小雨</span>-->
        <span id="showTime"></span>
    </div>

    <script>
        var t = null;
        t = setTimeout(time,1000);
        function time()
        {
            clearTimeout(t);
            dt = new Date();
            var y=dt.getFullYear();
            var mt=dt.getMonth()+1;
            var day=dt.getDate();
            var h=dt.getHours();
            var m=dt.getMinutes();
            var s=dt.getSeconds();
            // document.getElementById("showTime").innerHTML = y+"年"+mt+"月"+day+"日"+"-"+h+"时"+m+"分"+s+"秒";
            t = setTimeout(time,1000);
        }



    </script>


</div>
<div class="mainbox">
    <ul class="clearfix">
        <li>
            <div class="boxall" style="height: 3.2rem">
                <div class="alltitle">Average Product Ratings Across Categories</div>
                <div class="allnav" id="echart1"></div>
                <div class="boxfoot"></div>
            </div>
            <div class="boxall" style="height: 3.2rem">
                <div class="alltitle">Total Count of Products Utilizing Specific Discounts</div>
                <div class="allnav" id="echart2"></div>
                <div class="boxfoot"></div>
            </div>
            <div class="boxall" style="height: 3.2rem">
                <div class="alltitle">Top 10 Brands with the Highest Product Offerings</div>
                <div class="allnav" id="echart3"></div>
                <div class="boxfoot"></div>
            </div>
        </li>
        <li>
            <div class="bar">
                <div class="barbox">
                    <ul class="clearfix">
                        <li class="pulll_left counter" id="pcount"></li>
                        <li class="pulll_left counter" id="ocount"></li>
                    </ul>
                </div>
                <div class="barbox2">
                    <ul class="clearfix">
                        <li class="pulll_left">Total Product Inventory</li>
                        <li class="pulll_left">Out-of-Stock Product Count</li>
                    </ul>
                </div>
            </div>
            <div class="map">
                <div class="map1"><img src="picture/lbx.png"></div>
                <div class="map2"><img src="picture/jt.png"></div>
                <div class="map3"><img src="picture/map.png"></div>
                <div class="map4" id="map_1"></div>
            </div>
        </li>
        <li>
            <div class="boxall" style="height:3.4rem">
                <div class="alltitle">Product Count Across Various Categories</div>
                <div class="allnav" id="echart4"></div>
                <div class="boxfoot"></div>
            </div>
            <div class="boxall" style="height: 3.2rem">
                <div class="alltitle">Top 5 Subcategories with the Largest Product Range</div>
                <div class="allnav" id="echart5"></div>
                <div class="boxfoot"></div>
            </div>
            <div class="boxall" style="height: 3rem">
                <div class="alltitle">Top 10 Sellers with the Largest Product Offerings</div>
                <div class="allnav" id="echart6"></div>
                <div class="boxfoot"></div>
            </div>
        </li>
    </ul>
</div>
<div class="back"></div>

<%-- 产品数量 --%>
<script type="text/javascript">
    window.onload = function(){
        getproductcount();
    };

    window.setInterval(getproductcount,5000);

    function getproductcount(){
        $.ajax({
            url: "/productcount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                $('#pcount').text(data.count);
            },
            error: function(xhr, status, error) {
                console.log(error);
            }
        });
    }
</script>

<%-- 有多少产品处于缺货状态 --%>
<script type="text/javascript">
    window.onload = function(){
        getoutofstockcount();
    };

    window.setInterval(getoutofstockcount,5000);

    function getoutofstockcount(){
        $.ajax({
            url: "/outofstockcount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                $('#ocount').text(data.count);
            },
            error: function(xhr, status, error) {
                console.log(error);
            }
        });
    }
</script>

<!--echart1-->
<script type="text/javascript">

    window.onload = function(){
        getcategoryaveragerating();
    };

    window.setInterval(getcategoryaveragerating,5000);

    function getcategoryaveragerating(){
        var result = [];
        var x = [];
        var y = [];
        $.ajax({
            url: "/categoryaveragerating",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        for (var i = 0; i < result.length; i++) {
            x.push(result[i].category);
            y.push(result[i].averageRating);
        }
        console.log(x);
        console.log(y);
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart1'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                // text: '不同类别的产品的平均评分',
                // textStyle: {
                //     color: "white",
                //     fontSize: 12
                // },
            },
            tooltip: {
                trigger: "axis",
                formatter: '{b} <br/>{a}: {c}'
            },
            legend: {
                data:[
                    "Average rating"
                ],
                textStyle: {
                    color: "#89A0BF"
                },
                left: 220,
            },
            xAxis: {
                data: x,
                type: 'category',
                name:"category",
                nameTextStyle: {
                    color: "white",
                    fontSize: 10,
                },
                axisLabel: {
                    textStyle: {
                        color: "white"
                    }
                }
            },
            yAxis: [
                {
                    name:"rating",
                    type:'value',
                    nameTextStyle: {
                        color: "white"
                    },
                    axisLabel: {
                        textStyle: {
                            color: "white"
                        }
                    }
                }
            ],
            grid: {
                /* top: '10%',
                left: 10,
                right: 100,
                height: 250, */
                x:60,
                y:45,
                x2:60,
                y2:25
            },
            /* dataZoom: [
                {
                type: "inside"
                }
            ], */
            series: [
                {
                    name: 'Average rating',
                    type: 'bar',
                    radius: '15%',
                    barWidth: 30,
                    data: y,
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: "top",
                                textStyle: {
                                    color: "white"
                                }
                            },
                            color: "pink"
                        },
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

</script>

<!--echart2-->
<script type="text/javascript">

    window.onload = function(){
        getactualpriceanddiscount();
    };

    window.setInterval(getactualpriceanddiscount,5000);

    function getactualpriceanddiscount(){
        var result = [];
        var x = [];
        var y = [];
        $.ajax({
            url: "/discountcount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        for (var i = 0; i < result.length; i++) {
            x.push(result[i].dicount);
            y.push(result[i].count);
        }
        console.log(x);
        console.log(y);
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart2'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                // text: '原始价格与折扣的关系',
                // textStyle: {
                //     color: "white",
                //     fontSize: 12
                // },
            },
            tooltip: {
                trigger: "axis",
                formatter: '{b} <br/>{a}: {c}'
            },
            legend: {
                data:[
                    "discount"
                ],
                textStyle: {
                    color: "#89A0BF"
                },
                left: 220,
            },
            xAxis: {
                data: x,
                type: 'category',
                name:"discount",
                nameTextStyle: {
                    color: "white",
                    fontSize: 10,
                },
                axisLabel: {
                    textStyle: {
                        color: "white"
                    }
                }
            },
            yAxis: [
                {
                    name:"count",
                    type:'value',
                    nameTextStyle: {
                        color: "white"
                    },
                    axisLabel: {
                        textStyle: {
                            color: "white"
                        }
                    }
                }
            ],
            grid: {
                /* top: '10%',
                left: 10,
                right: 100,
                height: 250, */
                x:60,
                y:45,
                x2:60,
                y2:25
            },
            /* dataZoom: [
                {
                type: "inside"
                }
            ], */
            series: [
                {
                    name: 'discount rate',
                    type: 'scatter',
                    radius: '15%',
                    data: y,
                    itemStyle: {
                        normal: {
                            label: {
                                show: false,
                                position: "top",
                                textStyle: {
                                    color: "white"
                                }
                            },
                            color: "red"
                        },
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

</script>
<!--echarts3-->
<script type="text/javascript">

    window.onload = function(){
        getbrandcount();
    };

    window.setInterval(getbrandcount,5000);

    function getbrandcount(){
        var result = [];
        var x = [];
        var y = [];
        $.ajax({
            url: "/brandcount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        for (var i = 0; i < result.length; i++) {
            x.push(result[i].brand);
            y.push(result[i].count);
        }
        console.log(x);
        console.log(y);
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart3'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                // text: '不同品牌的产品数量TOP10',
                // textStyle: {
                //     color: "white",
                //     fontSize: 12
                // },
            },
            tooltip: {
                trigger: "axis",
                formatter: '{b} <br/>{a}: {c}'
            },
            legend: {
                data:[
                    "Product quantity"
                ],
                textStyle: {
                    color: "#89A0BF"
                },
                left: 220,
            },
            xAxis: {
                data: x,
                type: 'category',
                name:"brand",
                nameTextStyle: {
                    color: "white",
                    fontSize: 10,
                },
                axisLabel: {
                    textStyle: {
                        color: "white"
                    }
                }
            },
            yAxis: [
                {
                    name:"quantity",
                    type:'value',
                    nameTextStyle: {
                        color: "white"
                    },
                    axisLabel: {
                        textStyle: {
                            color: "white"
                        }
                    }
                }
            ],
            grid: {
                /* top: '10%',
                left: 10,
                right: 100,
                height: 250, */
                x:60,
                y:45,
                x2:60,
                y2:25
            },
            /* dataZoom: [
                {
                type: "inside"
                }
            ], */
            series: [
                {
                    name: 'Product quantity',
                    type: 'bar',
                    radius: '15%',
                    barWidth: 15,
                    data: y,
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: "top",
                                textStyle: {
                                    color: "white"
                                }
                            },
                            color: "cyan"
                        },
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

</script>
<!--echarts4-->
<script type="text/javascript">

    window.onload = function(){
        getcategorycount();
    };

    window.setInterval(getcategorycount,5000);

    function getcategorycount(){
        var result = [];
        var x = [];
        var y = [];
        var d = [];
        $.ajax({
            url: "/categorycount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        for (var i = 0; i < result.length; i++) {
            x.push(result[i].category);
            y.push(result[i].count);
            d.push({ value:result[i].count, name:result[i].category});
        }
        console.log(x);
        console.log(y);
        console.log(d);
        // 基于准备好的dom，初始化echarts实例e
        var myChart = echarts.init(document.getElementById('echart4'));

        // 指定图表的配置项和数据
        var option = {
            title: {
                // text: '不同类别的产品数量',
                // left:"center",
                // textStyle: {
                //     color: "white",
                //     fontSize: 12
                // },
            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                show: true,
                data: x,
                textStyle: {
                    color: "white",
                    fontSize: 10
                }
            },
            grid: {
                /*left: '200px',
                right: '9%',
                bottom: '3%',
                top: "9%",
                containLabel: true */
                /* x:60,
                y:10,
                x2:100,
                y2:150 */
            },
            color: ["red","yellow","cyan","pink"],
            series: [
                {
                    name: 'category',
                    type: 'pie',
                    radius: '50%',
                    center: ['70%', '50%'],
                    data: d,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
</script>
<!--echarts5-->
<script type="text/javascript">

    window.onload = function(){
        getsubcategorycount();
    };

    window.setInterval(getsubcategorycount,5000);

    function getsubcategorycount(){
        var result = [];
        var x = [];
        var y = [];
        $.ajax({
            url: "/subcategorycount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        for (var i = 0; i < result.length; i++) {
            x.push(result[i].subCategory);
            y.push(result[i].count);
        }
        console.log(x);
        console.log(y);
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart5'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                // text: '不同子类别的产品数量',
                // textStyle: {
                //     color: "white",
                //     fontSize: 12
                // },
            },
            tooltip: {
                trigger: "axis",
                formatter: '{b} <br/>{a}: {c}'
            },
            legend: {
                data:[
                    "Product quantity"
                ],
                textStyle: {
                    color: "#89A0BF"
                },
                left: 220,
            },
            xAxis: {
                type: 'value',
                boundaryGap: true,
                name:"quantity",
                nameTextStyle: {
                    color: "white"
                },
                axisLabel: {
                    textStyle: {
                        color: "white",
                    },
                }
            },
            yAxis: [
                {
                    type: 'category',
                    name:"subcategory",
                    nameLocation: 'start',
                    nameTextStyle: {
                        color: "white"
                    },
                    data: x,
                    axisLabel: {
                        rotate: 45, // 设置文本旋转角度，单位为度（0-180）
                        formatter: function (value) {
                            // 可以自定义 formatter 函数来处理文本内容，例如截取部分字符或添加换行符
                            if (value.length > 10) {
                                return value.substring(0, 10) + '...';
                            }
                            return value;
                        },
                        textStyle: {
                            color: "white",
                            fontSize: 10
                        },
                    },
                    inverse: true,
                }
            ],
            grid: {
                /* top: '10%',
                left: 10,
                right: 100,
                height: 250, */
                x:60,
                y:45,
                x2:60,
                y2:25
            },
            /* dataZoom: [
                {
                type: "inside"
                }
            ], */
            series: [
                {
                    name: 'Product quantity',
                    type: 'bar',
                    radius: '15%',
                    barWidth: 15,
                    data: y,
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: "right",
                                textStyle: {
                                    color: "white"
                                }
                            },
                            color: "green"
                        },
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

</script>
<!--echarts6-->
<script type="text/javascript">

    window.onload = function(){
        getsellercount();
    };

    window.setInterval(getsellercount,5000);

    function getsellercount(){
        var result = [];
        var x = [];
        var y = [];
        $.ajax({
            url: "/sellercount",
            type: "get",
            dataType: "json",
            async: false,
            success: function (data) {
                result = data;
            }
        });
        for (var i = 0; i < result.length; i++) {
            x.push(result[i].seller);
            y.push(result[i].count);
        }
        console.log(x);
        console.log(y);
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart6'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                // text: '不同卖家的产品数量TOP10',
                // textStyle: {
                //     color: "white",
                //     fontSize: 12
                // },
            },
            tooltip: {
                trigger: "axis",
                formatter: '{b} <br/>{a}: {c}'
            },
            legend: {
                data:[
                    "Product quantity"
                ],
                textStyle: {
                    color: "#89A0BF"
                },
                left: 220,
            },
            xAxis: {
                data: x,
                type: 'category',
                name:"seller",
                nameTextStyle: {
                    color: "white",
                    fontSize: 10,
                },
                axisLabel: {
                    textStyle: {
                        color: "white"
                    }
                }
            },
            yAxis: [
                {
                    name:"quantity",
                    type:'value',
                    nameTextStyle: {
                        color: "white"
                    },
                    axisLabel: {
                        textStyle: {
                            color: "white"
                        }
                    }
                }
            ],
            grid: {
                /* top: '10%',
                left: 10,
                right: 100,
                height: 250, */
                x:60,
                y:45,
                x2:60,
                y2:25
            },
            /* dataZoom: [
                {
                type: "inside"
                }
            ], */
            series: [
                {
                    name: 'Product quantity',
                    type: 'line',
                    radius: '15%',
                    data: y,
                    itemStyle: {
                        normal: {
                            label: {
                                show: false,
                                position: "top",
                                textStyle: {
                                    color: "white"
                                }
                            },
                            color: "yellow"
                        },
                    }
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

</script>

<!--map_1-->
<script>
    $(function map() {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('map_1'));
            var data = {{form.map_1.data|safe}};
            var geoCoordMap = {
                '海门':[121.15,31.89],
                '鄂尔多斯':[109.781327,39.608266],
                '招远':[120.38,37.35],
                '舟山':[122.207216,29.985295],
                '齐齐哈尔':[123.97,47.33],
                '盐城':[120.13,33.38],
                '赤峰':[118.87,42.28],
                '青岛':[120.33,36.07],
                '乳山':[121.52,36.89],
                '金昌':[102.188043,38.520089],
                '泉州':[118.58,24.93],
                '莱西':[120.53,36.86],
                '日照':[119.46,35.42],
                '胶南':[119.97,35.88],
                '南通':[121.05,32.08],
                '拉萨':[91.11,29.97],
                '云浮':[112.02,22.93],
                '梅州':[116.1,24.55],
                '文登':[122.05,37.2],
                '上海':[121.48,31.22],
                '攀枝花':[101.718637,26.582347],
                '威海':[122.1,37.5],
                '承德':[117.93,40.97],
                '厦门':[118.1,24.46],
                '汕尾':[115.375279,22.786211],
                '潮州':[116.63,23.68],
                '丹东':[124.37,40.13],
                '太仓':[121.1,31.45],
                '曲靖':[103.79,25.51],
                '烟台':[121.39,37.52],
                '福州':[119.3,26.08],
                '瓦房店':[121.979603,39.627114],
                '即墨':[120.45,36.38],
                '抚顺':[123.97,41.97],
                '玉溪':[102.52,24.35],
                '张家口':[114.87,40.82],
                '阳泉':[113.57,37.85],
                '莱州':[119.942327,37.177017],
                '湖州':[120.1,30.86],
                '汕头':[116.69,23.39],
                '昆山':[120.95,31.39],
                '宁波':[121.56,29.86],
                '湛江':[110.359377,21.270708],
                '揭阳':[116.35,23.55],
                '荣成':[122.41,37.16],
                '连云港':[119.16,34.59],
                '葫芦岛':[120.836932,40.711052],
                '常熟':[120.74,31.64],
                '东莞':[113.75,23.04],
                '河源':[114.68,23.73],
                '淮安':[119.15,33.5],
                '泰州':[119.9,32.49],
                '南宁':[108.33,22.84],
                '营口':[122.18,40.65],
                '惠州':[114.4,23.09],
                '江阴':[120.26,31.91],
                '蓬莱':[120.75,37.8],
                '韶关':[113.62,24.84],
                '嘉峪关':[98.289152,39.77313],
                '广州':[113.23,23.16],
                '延安':[109.47,36.6],
                '太原':[112.53,37.87],
                '清远':[113.01,23.7],
                '中山':[113.38,22.52],
                '昆明':[102.73,25.04],
                '寿光':[118.73,36.86],
                '盘锦':[122.070714,41.119997],
                '长治':[113.08,36.18],
                '深圳':[114.07,22.62],
                '珠海':[113.52,22.3],
                '宿迁':[118.3,33.96],
                '咸阳':[108.72,34.36],
                '铜川':[109.11,35.09],
                '平度':[119.97,36.77],
                '佛山':[113.11,23.05],
                '海口':[110.35,20.02],
                '江门':[113.06,22.61],
                '章丘':[117.53,36.72],
                '肇庆':[112.44,23.05],
                '大连':[121.62,38.92],
                '临汾':[111.5,36.08],
                '吴江':[120.63,31.16],
                '石嘴山':[106.39,39.04],
                '沈阳':[123.38,41.8],
                '苏州':[120.62,31.32],
                '茂名':[110.88,21.68],
                '嘉兴':[120.76,30.77],
                '长春':[125.35,43.88],
                '胶州':[120.03336,36.264622],
                '银川':[106.27,38.47],
                '张家港':[120.555821,31.875428],
                '三门峡':[111.19,34.76],
                '锦州':[121.15,41.13],
                '南昌':[115.89,28.68],
                '柳州':[109.4,24.33],
                '三亚':[109.511909,18.252847],
                '自贡':[104.778442,29.33903],
                '吉林':[126.57,43.87],
                '阳江':[111.95,21.85],
                '泸州':[105.39,28.91],
                '西宁':[101.74,36.56],
                '宜宾':[104.56,29.77],
                '呼和浩特':[111.65,40.82],
                '成都':[104.06,30.67],
                '大同':[113.3,40.12],
                '镇江':[119.44,32.2],
                '桂林':[110.28,25.29],
                '张家界':[110.479191,29.117096],
                '宜兴':[119.82,31.36],
                '北海':[109.12,21.49],
                '西安':[108.95,34.27],
                '金坛':[119.56,31.74],
                '东营':[118.49,37.46],
                '牡丹江':[129.58,44.6],
                '遵义':[106.9,27.7],
                '绍兴':[120.58,30.01],
                '扬州':[119.42,32.39],
                '常州':[119.95,31.79],
                '潍坊':[119.1,36.62],
                '重庆':[106.54,29.59],
                '台州':[121.420757,28.656386],
                '南京':[118.78,32.04],
                '滨州':[118.03,37.36],
                '贵阳':[106.71,26.57],
                '无锡':[120.29,31.59],
                '本溪':[123.73,41.3],
                '克拉玛依':[84.77,45.59],
                '渭南':[109.5,34.52],
                '马鞍山':[118.48,31.56],
                '宝鸡':[107.15,34.38],
                '焦作':[113.21,35.24],
                '句容':[119.16,31.95],
                '北京':[116.46,39.92],
                '徐州':[117.2,34.26],
                '衡水':[115.72,37.72],
                '包头':[110,40.58],
                '绵阳':[104.73,31.48],
                '乌鲁木齐':[87.68,43.77],
                '枣庄':[117.57,34.86],
                '杭州':[120.19,30.26],
                '淄博':[118.05,36.78],
                '鞍山':[122.85,41.12],
                '溧阳':[119.48,31.43],
                '库尔勒':[86.06,41.68],
                '安阳':[114.35,36.1],
                '开封':[114.35,34.79],
                '济南':[117,36.65],
                '德阳':[104.37,31.13],
                '温州':[120.65,28.01],
                '九江':[115.97,29.71],
                '邯郸':[114.47,36.6],
                '临安':[119.72,30.23],
                '兰州':[103.73,36.03],
                '沧州':[116.83,38.33],
                '临沂':[118.35,35.05],
                '南充':[106.110698,30.837793],
                '天津':[117.2,39.13],
                '富阳':[119.95,30.07],
                '泰安':[117.13,36.18],
                '诸暨':[120.23,29.71],
                '郑州':[113.65,34.76],
                '哈尔滨':[126.63,45.75],
                '聊城':[115.97,36.45],
                '芜湖':[118.38,31.33],
                '唐山':[118.02,39.63],
                '平顶山':[113.29,33.75],
                '邢台':[114.48,37.05],
                '德州':[116.29,37.45],
                '济宁':[116.59,35.38],
                '荆州':[112.239741,30.335165],
                '宜昌':[111.3,30.7],
                '义乌':[120.06,29.32],
                '丽水':[119.92,28.45],
                '洛阳':[112.44,34.7],
                '秦皇岛':[119.57,39.95],
                '株洲':[113.16,27.83],
                '石家庄':[114.48,38.03],
                '莱芜':[117.67,36.19],
                '常德':[111.69,29.05],
                '保定':[115.48,38.85],
                '湘潭':[112.91,27.87],
                '金华':[119.64,29.12],
                '岳阳':[113.09,29.37],
                '长沙':[113,28.21],
                '衢州':[118.88,28.97],
                '廊坊':[116.7,39.53],
                '菏泽':[115.480656,35.23375],
                '合肥':[117.27,31.86],
                '武汉':[114.31,30.52],
                '大庆':[125.03,46.58]
            };
            var convertData = function (data) {
                var res = [];
                for (var i = 0; i < data.length; i++) {
                    var geoCoord = geoCoordMap[data[i].name];
                    if (geoCoord) {
                        res.push({
                            name: data[i].name,
                            value: geoCoord.concat(data[i].value)
                        });
                    }
                }
                return res;
            };

            option = {
                tooltip : {
                    trigger: 'item',
                    formatter: function (params) {
                        if(typeof(params.value)[2] == "undefined"){
                            return params.name + ' : ' + params.value;
                        }else{
                            return params.name + ' : ' + params.value[2];
                        }
                    }
                },

                geo: {
                    map: 'china',
                    label: {
                        emphasis: {
                            show: false
                        }
                    },
                    roam: false,//禁止其放大缩小
                    itemStyle: {
                        normal: {
                            areaColor: '#4c60ff',
                            borderColor: '#002097'
                        },
                        emphasis: {
                            areaColor: '#293fff'
                        }
                    }
                },
                series : [
                    {
                        name: '消费金额',
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        data: convertData(data),
                        symbolSize: function (val) {
                            return val[2] / {{form.map_1.symbolSize}};
                        },
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#ffeb7b'
                            }
                        }
                    }
                ]
            };

            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
        }
    )




</script>
</body>
</html>
