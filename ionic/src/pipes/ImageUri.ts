import { Pipe } from '@angular/core';
import { environment } from '../../environments/environment';

/**
 * Add the base URL to image links
 */
@Pipe({
  name: 'imageUri'
})
export class ImageUri
{
  constructor() {}

  transform(input, arg?) {
    return environment.baseUrl + input;
  }
}
