import { HttpService } from './http.service';
import { CommonProto, StoreProto } from '@proto';
import PageablePb = CommonProto.PageablePb;
import StoreVisitResponse = StoreProto.StoreVisitResponse;
import StoreVisitRequest = StoreProto.StoreVisitRequest;
import StoreListResponse = StoreProto.StoreListResponse;
import StoreListRequest = StoreProto.StoreListRequest;

export class Main {
  private httpService = new HttpService('http://127.0.0.1:8080');

  constructor() {
    Main.addClickListener('getStoreButton', () => this.getStore());
    Main.addClickListener('getStoresButton', () => this.getStores());
  }

  private static addClickListener(id: string, listener: () => void) {
    const pageable: PageablePb = PageablePb.create({});
    console.log(pageable);
    const element = document.getElementById(id);
    element && element.addEventListener('click', listener);
  }

  getStore(): void {
    const storeIdElement = document.getElementById('storeId') as HTMLInputElement;
    const storeId = storeIdElement?.value ?? '12345';
    const customerElement = document.getElementById('customer') as HTMLInputElement;
    const customer = customerElement?.value ?? 'Tom';
    this.httpService
      .request<StoreVisitResponse>(
        StoreVisitRequest.create({ customer: customer, id: storeId })
      )
      .then(
        (res) => console.log(res),
        (error) => console.log(error)
      );
  }

  getStores(): void {
    const indexElement = document.getElementById('pageIndex') as HTMLInputElement;
    const pageIndex = Number(indexElement?.value ?? '1');
    const sizeElement = document.getElementById('pageSize') as HTMLInputElement;
    const pageSize = Number(sizeElement?.value ?? '3');
    this.httpService
      .request<StoreListResponse>(
        StoreListRequest.create({ pageable: { page: pageIndex, size: pageSize } })
      )
      .then(
        (res) => console.log(res),
        (error) => console.log(error)
      );
  }
}

new Main();