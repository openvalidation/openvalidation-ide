import { Component, Input } from '@angular/core';

@Component({
  selector: 'ovide-gauge-chart',
  templateUrl: './gauge-chart.component.html',
  styleUrls: ['./gauge-chart.component.scss']
})
export class GaugeChartComponent {

  @Input() chartTitle: string;
  @Input() strict = false;

  private _value: number;

  @Input() set value(value: number) {
    this._value = value;
  }

  constructor() { }

  public getValue() {
    return this._value;
  }


  getStrokeDash() {
    return `${this.getValue()}, 100`;
  }

  getColorClass() {
      if ((this.strict && this.getValue() >= 100)
        || (!this.strict && this.getValue() >= 60)) {
        return 'success';
      } else if ((this.strict && this.getValue() >= 60)
        || (!this.strict && this.getValue() >= 30)) {
        return 'mediocre';
      } else {
        return 'fail';
      }
  }
}
