import { Component, OnInit } from '@angular/core';
import { ThemeService } from '@ovide/services/theme.service';

@Component({
  selector: 'ovide-logo',
  templateUrl: './ovide-logo.component.html',
  styleUrls: ['./ovide-logo.component.scss']
})
export class OvideLogoComponent implements OnInit {

  imagePath: string;

  constructor(public themeService: ThemeService) { }

  ngOnInit(): void {
  }

}
