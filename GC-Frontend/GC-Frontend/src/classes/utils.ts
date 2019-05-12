export class Utils {
  static fixAmount(amount) {
    if (!isNaN(parseFloat(amount))) {
      return parseFloat(amount).toFixed(2);
    }
  }

  static decimalAdjust(type, value, exp) {
    // Se exp è undefined o zero...
    if (typeof exp === 'undefined' || +exp === 0) {
      return Math[type](value);
    }
    value = +value;
    exp = +exp;
    // Se value non è un numero o exp non è un intero...
    if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0)) {
      return NaN;
    }
    // Se value è negativo...
    if (value < 0) {
      return -Utils.decimalAdjust(type, -value, exp);
    }
    // Shift
    value = value.toString().split('e');
    value = Math[type](+(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp)));
    // Shift back
    value = value.toString().split('e');
    return +(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp));
  }

  // Decimal round
  static round10(value, exp) {
    return Utils.decimalAdjust('round', value, exp);
  }
}
