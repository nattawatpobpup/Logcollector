import {Component, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { HttpClient} from '@angular/common/http';
import { DomSanitizer} from '@angular/platform-browser';
import {MatPaginator, MatSnackBar, MatTableDataSource} from '@angular/material';
import {FormControl} from '@angular/forms';
import { FieldService } from '../shared/field/field.service';

export interface Index {
  value: string;
  viewValue: string;
}
export interface Type {
  value: string;
  viewValue: string;
}
@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.css']
})
export class QueryComponent implements OnInit {
  // tslint:disable-next-line:ban-types
  http: String = 'http://localhost:8080/';
  data: Array<any>;
  loading: boolean;
  inputData: any = {
    Index: '',

    Is1: false,
    Key1: '',
    Value1: '',

    Type1: '',
    Is2: false,
    Key2: '',
    Value2: '',

    Type2: '',
    Is3: false,
    Key3: '',
    Value3: '',

    Type3: '',
    Is4: false,
    Key4: '',
    Value4: '',

    Type4: '',
    Is5: false,
    Key5: '',
    Value5: '',


    Daygte: '',
    Timegte: '',
    Daylte: '',
    Timelte: '',

    selectget: '',

    foldersave: '',

  };
  indexs: Index[] = [
    {value: 'audit*', viewValue: 'audit*'},
    {value: 'fe-request-log*', viewValue: 'fe-request-log*'},
    {value: 'fe-api-log*', viewValue: 'fe-api-log*'},
  ];
  types: Type[] = [
    {value: '', viewValue: ''},
    {value: 'and', viewValue: 'AND'},
    {value: 'or', viewValue: 'OR'}
  ];
  field: Array<any>;

  toppings = new FormControl();
  status1: boolean;
  status2: boolean;
  status3: boolean;
  status4: boolean;
  dataColumns: string[] = ['data'];

  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar , private fieldService: FieldService, private sanitizer: DomSanitizer) {
    this.sanitizer = sanitizer;
  }

  ngOnInit() {
    this.loading = false;
  }

  getfield(value: string) {

    if (value === 'audit*') {
      this.fieldService.getAuditfield().subscribe(data => {
        this.field = data;
        this.inputData.selectget = '';

        this.clear1();
        console.log(this.field);
      });
    } else if (value === 'fe-request-log*') {
      this.fieldService.getFeRequestLodfield().subscribe(data => {
        this.field = data;
        this.inputData.selectget = '';
        this.clear1();
        console.log(this.field);

      });
    } else if (value === 'fe-api-log*') {
      this.fieldService.getFeApiLodfield().subscribe(data => {
        this.field = data;
        this.inputData.selectget = '';
        this.clear1();
        console.log(this.field);

      });
    }

  }


  get() {
    if (this.loading === false) {
      this.loading = true;
      this.data = null;
      this.httpClient.post(this.http + 'gets', this.inputData)
        .subscribe(data => {
            console.log('SEARCH COMPLETE', data);
            this.snackBar.open('SEARCH COMPLETE', 'OK', {duration: 3000});
            this.data = Object.values(data);
            this.loading = false;

          }, error => {
            this.snackBar.open(error.error.message, 'OK', {duration: 3000});
            console.log(error);
            this.loading = false;
          }
        );
    } else {
      this.snackBar.open('BUSY', 'OK', {duration: 3000});
    }
  }
  saves() {
    if (this.loading === false) {
      this.loading = true;
      this.data = null;
      this.httpClient.post(this.http + 'save' , this.inputData)
        .subscribe(data => {
            console.log('EXPORT OK', data);
            this.snackBar.open('COMPLETE', 'OK', {duration: 3000});
            this.data = Object.values(data);
            this.loading = false;
          }, error => {
            this.snackBar.open(error.error.message, 'OK', {duration: 3000});
            console.log(error);
            this.loading = false;
          }
        );
    } else {
      this.snackBar.open('BUSY', 'OK', {duration: 3000});
    }
  }
  savescorrelation() {
    if (this.loading === false) {
      this.loading = true;
      this.data = null;
      this.httpClient.post(this.http + 'saveCorrelationID' , this.inputData)
        .subscribe(data => {
            console.log('EXPORT BY CORRELATION_ID OK', data);
            this.snackBar.open('COMPLETE', 'OK', {duration: 3000});

            this.data = Object.values(data);
            this.loading = false;
          }, error => {
            this.snackBar.open(error.error.message, 'OK', {duration: 3000});
            console.log(error);
            this.loading = false;
          }
        );
    } else {
      this.snackBar.open('BUSY', 'OK', {duration: 3000});
}
  }
  clear1() {
    this.inputData.Is1 = false;
    this.inputData.Key1 = '';
    this.inputData.Value1 = '';
    this.status1 = false;
    this.clear2();
  }
  clear2() {
    this.inputData.Type1 = '';
    this.inputData.Is2 = false;
    this.inputData.Key2 = '';
    this.inputData.Value2 = '';
    this.status2 = false;
    this.clear3();
  }
  clear3() {
    this.inputData.Type2 = '';
    this.inputData.Is3 = false;
    this.inputData.Key3 = '';
    this.inputData.Value3 = '';
    this.status3 = false;
    this.clear4();
  }
  clear4() {
    this.inputData.Type3 = '';
    this.inputData.Is4 = false;
    this.inputData.Key4 = '';
    this.inputData.Value4 = '';
    this.status4 = false;
    this.clear5();
  }
  clear5() {
    this.inputData.Type4 = '';
    this.inputData.Is5 = false;
    this.inputData.Key5 = '';
    this.inputData.Value5 = '';

  }
  clear() {
    this.clear1();
    this.inputData.Index = '';
    this.inputData.Daygte = '';
    this.inputData.Timegte = '';
    this.inputData.Daylte = '';
    this.inputData.Timelte = '';
    this.inputData.selectget = '';
    this.data = null;
    this.inputData.foldersave = '';
  }


  checkstatus1(Key1, Value1) {
    if ( Key1 === '' || Value1 === '') {
        this.status1 = false;
    } else {
        this.status1 = true;
    }
  }
  checkstatus2(Type1, Key2, Value2) {
    if ( Type1 === '' || Key2 === '' || Value2 === '') {
      this.status2 = false;
    } else {
      this.status2 = true;
    }
  }
  checkstatus3(Type2, Key3, Value3) {
    if ( Type2 === '' || Key3 === '' || Value3 === '') {
      this.status3 = false;
    } else {
      this.status3 = true;
    }
  }
  checkstatus4(Type3, Key4, Value4) {
    if ( Type3 === '' || Key4 === '' || Value4 === '') {
      this.status4 = false;
    } else {
      this.status4 = true;
    }
  }

}
