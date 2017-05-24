import { Component } from '@angular/core';
import { NavController, NavParams, ViewController } from 'ionic-angular';
import { FoodType } from "../../models/FoodType";

@Component({
  selector: 'page-select-food',
  templateUrl: 'select-food.html',
})
export class SelectFoodPage {
  menus = [
    {'value': 'VEGI', 'label': 'Vegi', 'icon': 'nutrition'},
    {'value': 'MEAT', 'label': 'Meat', 'icon': 'restaurant'},
    {'value': 'NONE', 'label': 'None', 'icon': 'remove-circle'}
  ];

  food: FoodType;
  drink: boolean;

  constructor(public navCtrl: NavController, public navParams: NavParams, private viewCtrl: ViewController) {
  }

  dismiss() {
    this.viewCtrl.dismiss({ drink: false, selectedFood: FoodType.NONE });
  }

  save() {
    console.log(this.drink);
    console.log(this.food);
    this.viewCtrl.dismiss({ drink: this.drink, selectedFood: this.food });
  }
}
