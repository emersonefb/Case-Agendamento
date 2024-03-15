import {TestBed} from '@angular/core/testing';

import {AppEffectService} from './app.effect.service';

describe('AppEffectService', () => {
  let service: AppEffectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppEffectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
