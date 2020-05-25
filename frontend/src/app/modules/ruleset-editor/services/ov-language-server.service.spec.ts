import { TestBed } from '@angular/core/testing';

import { OVLanguageServerService } from './ov-language-server.service';

describe('LanguageServerService', () => {
  let service: OVLanguageServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OVLanguageServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
