export class Utils {
  static fixAmount(amount) {
    if (!isNaN(parseFloat(amount))) {
      return parseFloat(amount).toFixed(2);
    }
  }
}
