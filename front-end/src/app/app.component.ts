import {Component} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {appTokenAction, IAppStage} from './store/app.stage';
import {map} from 'rxjs';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  title = 'front-end';



  tokenJWT$ = this.store.select('app').pipe(
    map(app => app.tokenJWT)
  )

  constructor(
    private store: Store<{ app: IAppStage}>,
    private router: Router,
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer,
) {
  iconRegistry.addSvgIcon(
    'hemo',
    sanitizer.bypassSecurityTrustResourceUrl('assets/images/home.svg'));
  iconRegistry.addSvgIcon(
    'brazil',
    sanitizer.bypassSecurityTrustResourceUrl('assets/images/brazil.svg'));
}

navigate(menu: string) {
    this.router.navigate([menu]);
    this.store.dispatch(appTokenAction())
}
}
