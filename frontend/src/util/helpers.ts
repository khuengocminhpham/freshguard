export const dateToArray = (date: Date): number[] => {
  return [date.getFullYear(), date.getMonth() + 1, date.getDate()];
};

export const arrayToDate = (dateArray: number[]): string => {
  if (dateArray.length >= 3) {
    return new Date(
      dateArray[0],
      dateArray[1] - 1,
      dateArray[2]
    ).toLocaleDateString();
  }
  return new Date().toLocaleDateString();
};
