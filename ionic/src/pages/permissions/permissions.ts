import { Component } from '@angular/core';
import {ApiService} from "../../providers/api.service";
import {User} from "../../models/User";
import {RoleType} from "../../models/RoleType";
import {TranslatedSnackbarService} from "../../providers/translated-snackbar.service";

@Component({
  selector: 'page-permissions',
  templateUrl: 'permissions.html'
})
export class PermissionsPage {

  /**
   * Array of users
   */
  users: User[];

  /**
   * Copy of the user, so we can revert changes with "undo"
   */
  oldUser: User;

  constructor(private apiService: ApiService, private translatedSnackbarService: TranslatedSnackbarService) {
    this.refreshUsers();
  }

  /**
   * Fetch users from server
   */
  refreshUsers(){
    this.apiService.getUsersByRole(RoleType.ADMINISTRATOR).then((data) => this.users = data);
  }

  /**
   * Assign administrator role on select
   * @param user
   */
  onSelected(user: User) {
    this.oldUser = Object.assign({}, user);
    user.role = RoleType.ADMINISTRATOR;
    this.updateUser(user);
  }

  /**
   * Remove the role of a user, which makes him "REGISTERED"
   * @param $event
   * @param user
   */
  removeRole($event, user: User) {
    this.oldUser = Object.assign({}, user);
    user.role = RoleType.REGISTERED;
    this.updateUser(user);
  }


  /**
   * Send the updated user object to the server and persist it
   * @param user
   * @param showUndo
   */
  updateUser(user: User, showUndo = true) {
    this.apiService.updateUserPartial(user.id, {role: user.role}).then(() => {
      this.refreshUsers();
      this.translatedSnackbarService.showSnackbar('USER_UPDATED', 'UNDO', {firstName:user.firstName, lastName:user.lastName}).then(() => {
        this.updateUser(this.oldUser, false);
      });
    });
  }
}
