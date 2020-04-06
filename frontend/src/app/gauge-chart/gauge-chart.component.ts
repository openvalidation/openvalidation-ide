import { Component, Input } from '@angular/core';

@Component({
  selector: 'ovide-gauge-chart',
  templateUrl: './gauge-chart.component.html',
  styleUrls: ['./gauge-chart.component.scss']
})
export class GaugeChartComponent {

  private _value: number;

  @Input() set value(value: number) {
    this._value = value;
    console.log(value);
    // TODO change visualization
  }

  constructor() { }

}
