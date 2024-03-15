import {Platform} from '@angular/cdk/platform';
import {Component, inject} from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  public platform= inject(Platform);

}

