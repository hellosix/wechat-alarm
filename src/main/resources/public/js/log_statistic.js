/**
 * Created by lzz on 2018/4/2.
 */

$(document).ready(function () {
    var metricsMap = window.metricsMap;
    for(var role in metricsMap){
        if(metricsMap[role].length != 0 ){
            create_chart(metricsMap[role], role);
        }
    }

    var logs = window.logs;
    var str = "";
    for(var i = 0; i < logs.length; i++ ){
        str += "<tr>";
        str += "<td>"+ logs[i].id + "</td>";
        str += "<td>"+ logs[i].metric + "</td>";
        str += "<td>"+ logs[i].metric_value + "</td>";
        str += "<td>"+ logs[i].error_message + "</td>";
        str += "<td>"+ logs[i].error_code + "</td>";
        str += "<td>"+ timestampToTime(logs[i].add_time) + "</td>";
        str += "</tr>";
    }
    $("#log-detail-table").html( str );
    $("#command-table").dataTable({
        "aaSorting": [
            [ 0, "desc" ]
        ]
    } );
});

function create_chart(metrics, id){
    var labels = [];
    var counts = [];
    for( var i in metrics){
        labels.push( metrics[i]["date"] );
        counts.push( metrics[i]["c"] );
    }
    var config = {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: "count",
                backgroundColor: window.chartColors.red,
                borderColor: window.chartColors.red,
                data: counts,
                fill: false,
            }]
        },
        options: {
            responsive: true,
            legend: {
                display: false
            },
            title:{
                display:false,
                text:'Chart.js Line Chart'
            },
            tooltips: {
                mode: 'index',
                intersect: false,
            },
            hover: {
                mode: 'nearest',
                intersect: true
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: false,
                        labelString: 'metric'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: false,
                        labelString: 'Value'
                    }
                }]
            }
        }
    };

    var chart_div = '<div class="col-sm-4">' +
        '<div class="chart-header">' + id + '</div>' +
        '<canvas id="canvas' + id+ '" style="max-height: 200px"></canvas>'+
        '</div>';

    $("#chart-content").append( chart_div );
    var ctx = document.getElementById( "canvas" + id ).getContext("2d");
    new Chart(ctx, config);
}
