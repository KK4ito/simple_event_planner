import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions } from '@angular/http';


@Injectable()
export class AuthService {
  config: any;

  constructor(private http: Http) {
    this.config = {
      baseUrl: 'http://localhost:8080/api'
    };
  }

  /**
   * Get request to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param parameters
   * @returns {Promise<T>|Promise}
   */
  get(endpoint, parameters = ''): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.get(this.config.baseUrl + '/' + endpoint + '/' + parameters)
        .subscribe(data => {
          // wait until not uninit
          resolve(data.json());
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Puts an object to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param object
   * @returns {Promise<T>|Promise}
   */
  put(endpoint, object): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.put(this.config.baseUrl + '/' + endpoint + '/', object)
        .subscribe(data => {
          try { // TODO: Is there a better way to do this?
            resolve(data.json());
          } catch (e) {
            resolve();
          }
        }, error => {
          reject(error);
        });

    });
  }

  /**
   * Posts an object to an endpoint with all the required headers set automatically
   *
   * @param endpoint
   * @param object
   * @returns {Promise<T>|Promise}
   */
  post(endpoint, object): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.post(this.config.baseUrl + '/' + endpoint + '/', object)
        .subscribe(data => {
          resolve(data);
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Deletes an object
   *
   * @param endpoint
   * @returns {Promise<T>|Promise}
   */
  delete(endpoint): Promise<any> {
    return new Promise((resolve, reject) => {
      this.http.delete(this.config.baseUrl + '/' + endpoint + '/')
        .subscribe(data => {
          //this.log('delete response', data.json());
          //resolve(data.json());
          resolve();
        }, error => {
          reject(error);
        });
    });
  }

  /**
   * Uploads a file blob to the server
   *
   * @param endpoint string: The name of the endpoint
   * @param blob blob:
   * @param onProgress function: Gets called when progress is made (1-100)
   * @returns {Promise<T>|Promise}
   */
  /*
  uploadBlob(endpoint: string, blob: Blob, onProgress: (n: number) => any): Promise<any> {
    this.log('fn: uploadBlob');

    return new Promise((resolve, reject) => {

      let xhr:XMLHttpRequest = new XMLHttpRequest();
      xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            resolve(<File> JSON.parse(xhr.response));
          } else {
            reject(xhr.response);
          }
        }
      };

      xhr.upload.addEventListener("progress", function (oEvent:any) {
        if (oEvent.lengthComputable) {
          onProgress(oEvent.loaded / oEvent.total);
        }
      }, false);

      xhr.open('POST', this.config.baseUrl + endpoint, true);

      this.getRequestHeader().then(header => {
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.setRequestHeader("Authorization", header);

        let formData = new FormData();
        formData.append("file", blob);
        xhr.send(formData);
      }).catch(console.error);
    });
  }


  uploadFile(endpoint: string, fileUri: string, onProgress: (n: number) => any): Promise<any> {
    this.log('fn: uploadFile');

    return new Promise((resolve, reject) => {

      const fileTransfer = new Transfer();

      this.getRequestHeader().then(header => {

        let options = {
          fileKey: 'file',
          fileName: fileUri.substr(fileUri.lastIndexOf('/') + 1),
          headers: {
            "Authorization": header
          }
        };

        fileTransfer.upload(fileUri, this.config.baseUrl + endpoint, options)
          .then((res: any) => {
            if (res.responseCode) {
              if (res.responseCode === 200) {
                resolve(<File> JSON.parse(res.response));
              } else {
                reject(res.response);
              }
            } else {
              reject(res.exception);
            }
          })
          .catch(reject);

      }).catch(console.error);
    });
  }
*/
}
