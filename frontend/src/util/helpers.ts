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
export const isoStringToArray = (isoString: string): number[] => {
  const date = new Date(isoString);
  return [
    date.getFullYear(),
    date.getMonth() + 1,
    date.getDate(),
    date.getHours(),
    date.getMinutes(),
    date.getSeconds(),
  ];
};
export const arrayToISOString = (dateArray: number[]): string => {
  const [year, month = 1, day = 1, hours = 0, minutes = 0, seconds = 0] =
    dateArray;

  const date = new Date(year, month - 1, day, hours, minutes, seconds);
  return date.toISOString();
};
