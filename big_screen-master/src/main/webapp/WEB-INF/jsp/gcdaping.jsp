<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.bigdata.visualanalysis.bean.GCYearCount" %>
<%@page import="com.bigdata.visualanalysis.bean.GCPlatformCount" %>
<%@page import="com.bigdata.visualanalysis.bean.GCRatingCount" %>
<%@page import="com.bigdata.visualanalysis.bean.GCDateCount" %>
<%@page import="com.bigdata.visualanalysis.bean.GCHelpfulAndFunnyCount" %>
<%@page import="com.bigdata.visualanalysis.bean.GCRecommendedAndHour" %>
<%@page import="com.bigdata.visualanalysis.bean.GCTagCount" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Analysis of Game Recommendation on Steam</title>
    <!-- 设置标签图标 -->
    <link href="images/favicon.ico" rel="shortcut icon" />
    <!-- 引入需要的css文件 -->
    <link href="css/gc.css" rel="stylesheet" />
    <script src="js/echarts5.js"></script>
    <script src="js/echarts-wordcloud.js"></script>
</head>
<body>
<!-- 头部 -->
<div id="m_top">
    <div class="title">
        <h1>Analysis of Game Comment Data Based on Big Data</h1>
    </div>
</div>
<!-- 中部 -->
<div id="m_center">
    <div class="c1">
        <div class="c1_t" id="chart1">
            <script type="text/javascript">
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart1'));

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'Number of Games Released Per Year',
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        },
                    },
                    tooltip: {
                        trigger: "axis",
                        formatter: '{b} <br/>{a}: {c}'
                    },
                    legend: {
                        data:[
                            "Unit"
                        ],
                        textStyle: {
                            color: "#89A0BF"
                        },
                        left: 320,
                    },
                    xAxis: {
                        data: [
                            <c:forEach items="${yearCountList}" var ="a">
                            "${a.yr}",
                            </c:forEach>
                        ],
                        name:"Year",
                        nameTextStyle: {
                            color: "white"
                        },
                        axisLabel: {
                            textStyle: {
                                color: "white"
                            }
                        }
                    },
                    yAxis: [
                        {
                            name:"Number",
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
                            name: 'Game Number',
                            type: 'line',
                            radius: '15%',
                            data: [
                                <c:forEach items="${yearCountList}" var="a">
                                "${a.count}",
                                </c:forEach>
                            ],
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
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
            </script>
        </div>
        <div class="c1_m" id="chart2">
            <script type="text/javascript">
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart2'));
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'Game Distribution Across Different Systems',
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        /* trigger: 'item', */
                        formatter: '{b} <br/>{a}: {c}'
                    },
                    legend: {
                        data:[
                            "Unit"
                        ],
                        textStyle: {
                            color: "white"
                        },
                        left: 360,
                    },
                    grid: {
                        /* left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true */
                        x:60,
                        y:60,
                        x2:80,
                        y2:20
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'value',
                        boundaryGap: true,
                        name:"Number",
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
                            name:"platform",
                            nameLocation: 'start',
                            nameTextStyle: {
                                color: "white"
                            },
                            data: [
                                <c:forEach items="${platformCountList}" var="b">
                                "${b.platform}",
                                </c:forEach>
                            ],
                            axisLabel: {
                                textStyle: {
                                    color: "white",
                                    fontSize: 12
                                },
                            },
                            inverse: true,
                        }
                    ],
                    /*grid: {
                      /* top: '45%', */
                    /* left: 200, */
                    /* right: 100, */
                    /*height: 250,
                  },*/
                    series: [
                        {
                            name: 'Game Number',
                            type: 'bar',
                            barWidth: "40%",
                            data: [
                                <c:forEach items="${platformCountList}" var="b">
                                "${b.count}",
                                </c:forEach>
                            ],
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
                                        position: "right",
                                        textStyle: {
                                            color: "white"
                                        }
                                    },
                                    color: "cyan"
                                },
                            },
                        },
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            </script>
        </div>
        <div class="c1_b" id="chart3">
            <script type="text/javascript">
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart3'));

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'The Link Between Game Playtime and User Recommendation',
                        textStyle: {
                            color: "white",
                            fontSize: 11
                        },
                    },
                    tooltip: {
                        trigger: "axis",
                        formatter: '{b} <br/>{a}: {c}'
                    },
                    legend: {
                        data:[
                            "Average Playtime Hours"
                        ],
                        textStyle: {
                            color: "white",
                            fontSize: 10
                        },
                        left: 380,
                    },
                    xAxis: {
                        data: [
                            <c:forEach items="${recommendedAndHourList}" var ="b">
                            "${b.isrecommended}",
                            </c:forEach>
                        ],
                        name:"IsRecommended",
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
                            name:"Hour",
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
                            name: 'Average Playtime Hours',
                            type: 'bar',
                            radius: '15%',
                            barWidth: 30,
                            data: [
                                <c:forEach items="${recommendedAndHourList}" var="b">
                                "${b.avghour}",
                                </c:forEach>
                            ],
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
            </script>
        </div>
    </div>
    <div class="c2">
        <div class="c2_t" id="chart4">
            <script type="text/javascript">
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart4'));

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'Top 10 Most Reviewed Day For Games on Steam',
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        },
                    },
                    tooltip: {
                        trigger: "axis",
                        formatter: '{b} <br/>{a}: {c}'
                    },
                    legend: {
                        data:[
                            "Number of Reviews"
                        ],
                        textStyle: {
                            color: "white"
                        },
                        left: 220,
                    },
                    xAxis: {
                        data: [
                            <c:forEach items="${dateCountList}" var ="b">
                            "${b.dt}",
                            </c:forEach>
                        ],
                        name:"date",
                        nameTextStyle: {
                            color: "white",
                            fontSize: 10
                        },
                        axisLabel: {
                            textStyle: {
                                color: "white"
                            }
                        }
                    },
                    yAxis: [
                        {
                            name:"number",
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
                            name: 'Number of Reviews',
                            type: 'bar',
                            radius: '15%',
                            barWidth: 15,
                            data: [
                                <c:forEach items="${dateCountList}" var="b">
                                "${b.count}",
                                </c:forEach>
                            ],
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
                                        position: "top",
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
            </script>
        </div>
        <div class="c2_b" id="chart5">
            <script type="text/javascript">
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart5'));
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'Analysis of User Reactions to Recommendation Reviews on Steam',
                        left: 'center',
                        top: '20',
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        },
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} <br/>{a}: {c}'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        top: '40',
                        data:[
                            <c:forEach items="${helpfulAndFunnyCountList}" var="b">
                            "${b.hfmark}",
                            </c:forEach>
                        ],
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        },
                    },
                    color: ["purple","yellow"],
                    series: [
                        {
                            name: 'times',
                            type: 'pie',
                            radius: '50%',
                            center: ['50%', '60%'],
                            data: [
                                <c:forEach items="${helpfulAndFunnyCountList}" var="b">
                                { value: "${b.count}", name: "${b.hfmark}" },
                                </c:forEach>
                            ],
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
            </script>
        </div>
    </div>
    <div class="c3">
        <div class="c3_t" id="chart6">
            <script type="text/javascript">
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('chart6'));
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'Users\' Review of Games on Steam',
                        left: 'center',
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        },
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} <br/>{a}: {c}'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data:[
                            <c:forEach items="${ratingCountList}" var="b">
                            "${b.rating}",
                            </c:forEach>
                        ],
                        textStyle: {
                            color: "white",
                            fontSize: 10
                        },
                    },
                    // color: ["red","green"],
                    series: [
                        {
                            name: 'Number',
                            type: 'pie',
                            radius: '50%',
                            center: ['70%', '60%'],
                            data: [
                                <c:forEach items="${ratingCountList}" var="b">
                                { value: "${b.count}", name: "${b.rating}" },
                                </c:forEach>
                            ],
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
            </script>
        </div>
        <div class="c3_b" id="chart7">
            <script>
                var myChart = echarts.init(document.getElementById('chart7'));
                myChart.setOption({
                    title: {
                        text: 'Number of Game Tags on Steam',
                        textStyle: {
                            color: "white",
                            fontSize: 12
                        },
                    },
                    tooltip: {},
                    series: [{
                        type : 'wordCloud',  //类型为字符云
                        shape:'smooth',  //平滑
                        gridSize : 8, //网格尺寸
                        size : ['50%','50%'],
                        //sizeRange : [ 50, 100 ],
                        rotationRange : [-45, 0, 45, 90], //旋转范围
                        textStyle : {
                            normal : {
                                fontFamily:'微软雅黑',
                                color: function() {
                                    return 'rgb(' +
                                        Math.round(Math.random() * 255) +
                                        ', ' + Math.round(Math.random() * 255) +
                                        ', ' + Math.round(Math.random() * 255) + ')'
                                }
                            },
                            emphasis : {
                                shadowBlur : 5,  //阴影距离
                                shadowColor : '#333'  //阴影颜色
                            }
                        },
                        left: 'center',
                        top: '15',
                        right: null,
                        bottom: null,
                        width:'100%',
                        height:'100%',
                        data:[
                            <c:forEach items="${tagCountList}" var ="u">
                            {
                                name: "${u.tag}",
                                value: "${u.count}",
                            },
                            </c:forEach>
                        ]
                    }]
                });
            </script>
        </div>
    </div>
</div>
</body>
</html>