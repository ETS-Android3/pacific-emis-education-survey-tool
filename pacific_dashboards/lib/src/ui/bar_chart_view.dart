import 'package:flutter/material.dart';

import 'package:charts_flutter/flutter.dart' as charts;

class BarChartView extends StatefulWidget {
  final data;
  final title;

  BarChartView({Key key, this.title, this.data}) : super(key: key);

  @override
  BarChartViewState createState() => new BarChartViewState();
}

class TeachersByStateData {
  final String domain;
  final int measure;

  TeachersByStateData(this.domain, this.measure);
}

class BarChartViewState extends State<BarChartView> {
  @override
  Widget build(BuildContext context) {
    List<TeachersByStateData> data = [];
    widget.data.forEach((k, v) {
      data.add(new TeachersByStateData(k, v));
    });

    var series = [
      new charts.Series(
        domainFn: (TeachersByStateData teachersData, _) => teachersData.domain,
        measureFn: (TeachersByStateData teachersData, _) =>
            teachersData.measure,
        colorFn: (TeachersByStateData teachersData, _) =>
            charts.MaterialPalette.white,
        id: "name",
        data: data,
      ),
    ];

    return new charts.BarChart(
      series,
      animate: true,
      primaryMeasureAxis: new charts.NumericAxisSpec(
          renderSpec: new charts.GridlineRendererSpec(
              labelStyle: new charts.TextStyleSpec(
                  fontSize: 8, color: charts.MaterialPalette.white),
              lineStyle: new charts.LineStyleSpec(
                  color: charts.MaterialPalette.blue.shadeDefault))),
      domainAxis: new charts.OrdinalAxisSpec(
        renderSpec: new charts.SmallTickRendererSpec(
            labelStyle: new charts.TextStyleSpec(
                fontSize: 12,
                color: charts.MaterialPalette.deepOrange.shadeDefault),
            lineStyle: new charts.LineStyleSpec(
                color: charts.MaterialPalette.green.shadeDefault)),
      ),
    );
  }
}
