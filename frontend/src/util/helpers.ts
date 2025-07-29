export const dateToArray = (date: Date): number[] => {
  return [date.getFullYear(), date.getMonth() + 1, date.getDate()];
};

export const arrayToDate = (dateArray: number[]): string => {
  if (dateArray.length >= 3) {
    const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2]);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // add 1 since month is 0-indexed
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`; // format: YYYY-MM-DD
  }
  return new Date().toISOString().split("T")[0];
};
