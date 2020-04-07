import { Component, Input } from '@angular/core';

@Component({
  selector: 'ovide-gauge-chart',
  templateUrl: './gauge-chart.component.html',
  styleUrls: ['./gauge-chart.component.scss']
})
export class GaugeChartComponent {

  @Input() chartTitle: string;

  private _value: number;

  @Input() set value(value: number) {
    this._value = value;
    console.log(value);
    // TODO change visualization
  }

  constructor() { }

  public getValue() {
    return this._value;
  }


  getStrokeDash() {
    return `${this.getValue()}, 100`;
  }

  getColor() {
    return 'rgb(255,159,0,0.5)'; // TODO Change based on value
  }
}
