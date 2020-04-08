import { Component, Input } from '@angular/core';

@Component({
  selector: 'ovide-gauge-chart',
  templateUrl: './gauge-chart.component.html',
  styleUrls: ['./gauge-chart.component.scss']
})
export class GaugeChartComponent {

  @Input() chartTitle: string;
  @Input() strict = false;

  private _value = 0;

  @Input() set value(value: number) {
    const toChange = value - this._value;
    const interval = setInterval(() => {
      if (toChange > 0) {
        this._value++;
      } else {
        this._value--;
      }
      if ((toChange >= 0 && (this._value + 1) >= value) || (toChange < 0 && (this._value - 1) <= value)) {
        this._value = Math.round(value * 10) / 10;
        clearInterval(interval);
      }
    }, (1000 / Math.abs(toChange)));
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
