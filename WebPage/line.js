

google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);


      
     var cnt = 0;
     var cntWind = 0;
     var cntTemperature = 0;
     var cntHumidity = 0;

     var jsonData = getJson();

          
           var rainarr = [
            ['Per 5 mins', 'Rain in mm'],
          ['', 0.000001 ],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001]
        ];
          
         function addToRainArr(){
             if(rainarr.length-1<=cnt){
                 for (var i = 1; i < rainarr.length-1; i++){
                         rainarr[i][1] = rainarr[i+1][1];
                 }
                 rainarr[rainarr.length-1][1]=jsonData.rainfall;
             }
             else
                for (var i = 1; i < rainarr.length; i++){
                    if(rainarr[i][1]== 0.000001){
                        cnt++;
                        rainarr[i][1] = jsonData.rainfall;
                        break;    
                    }                  
                }
        }
        var windarr = [['Per 5 mins', 'Wind in km/h'],
          ['', 11 ],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001]
        ];

        function addToWindArr(){
             if(windarr.length-1==cntWind){
                     for(var i = 1; i<windarr.length-1;i++){
                         windarr[i][1] = windarr[i][1];
                     }
                    windarr[windarr.length-1 ][1]=jsonData.wind_speed;
             }
            else
                for (var i = 0; i < windarr.length; i++){
                    if(windarr[i][1]== 0.000001){
                        cntWind++;
                        windarr[i][1] = jsonData.wind_speed;
                        break;    
                    }                  
                }
        }

        var temparaturearr = [['Per 5 mins', 'Temparature in °C'],
          ['', 0.000001 ],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001]
        ];

        function addToTemperatureArr(){
             if(temparaturearr.length-1==cntTemperature){
                     for(var i = 1; i<temparaturearr.length-1;i++){
                         temparaturearr[i][1] = temparaturearr[i][1];
                     }
                    temparaturearr[temparaturearr.length-1][1]=jsonData.ambient_temperature;
             }
            else
                for (var i = 0; i < temparaturearr.length; i++){
                    if(temparaturearr[i][1]== 0.000001){
                        cntTemperature++;
                        temparaturearr[i][1] = jsonData.ambient_temperature;
                        break;    
                    }                  
                }
        }
         var humidityarr =[['Per 5 mins', 'Humidity in %'],
          ['', 0.000001 ],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001],
          ['',  0.000001]
        ];

        function addToHumidityArr(){
            if(humidityarr.length-1==cntHumidity){
                     for(var i = 1; i<humidityarr.length-1;i++){
                         humidityarr[i][1] = humidityarr[i][1];
                     }
                    humidityarr[humidityarr.length-1][1]=jsonData.humidity;
             }
            else
                for (var i = 0; i < humidityarr.length; i++){
                    if(humidityarr[i][1]== 0.000001){
                        cntHumidity++;
                        humidityarr[i][1] = jsonData.humidity;
                        break;    
                    }                  
                }
        }
    
      function drawChart() {
          draw();
          setInterval(draw,300000);
      }

    function getJson(){
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", "http://172.18.252.131:8080/last", false); // false for synchronous request
        xmlHttp.send(null);
        jsonData = JSON.parse(xmlHttp.responseText);
        return xmlHttp.responseText;
    }

    function draw(){
         getJson(); 
         addToRainArr();
        
        var str = "";
        for (var i = 0; i < rainarr.length; i++)
            str += rainarr[i] + " ";
        console.log(str);
          
        var rainData = google.visualization.arrayToDataTable(rainarr);

        var rainOptions = {
          title: 'Rain:',
          hAxis: {title: 'Per 5 mins',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0},
            series:{
                0:{color: '#0d47a1'},
            }
        };
        addToWindArr();
          
        var windData = google.visualization.arrayToDataTable(windarr);

        var windOptions = {
          title: 'Wind:',
          hAxis: {title: 'Per 5 mins',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0},
            series:{
              0:{color: '#81d4fa'},
            }
        };
          
          addToTemperatureArr();
        var tempData = google.visualization.arrayToDataTable(temparaturearr);

        var tempOptions = {
          title: 'Temperature:',
          hAxis: {title: 'Per 5 mins',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0},
            series:{
                0:{color: '#ffcc00'},
            }


        };

          addToHumidityArr();
          
        var humData = google.visualization.arrayToDataTable(humidityarr);

        var humOptions = {
          title: 'Humidity:',
          hAxis: {title: 'Per 5 mins',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0},
            series:{
                0:{color: '#18ffff'},
            }
        };
        var chart = new google.visualization.AreaChart(document.getElementById('chart_rain'));
        chart.draw(rainData, rainOptions);
          
        var chart = new google.visualization.AreaChart(document.getElementById('chart_wind'));
        chart.draw(windData, windOptions);
          
        var chart = new google.visualization.AreaChart(document.getElementById('chart_temp'));
        chart.draw(tempData, tempOptions);
          
        var chart = new google.visualization.AreaChart(document.getElementById('chart_humity'));
        chart.draw(humData, humOptions);
      }
