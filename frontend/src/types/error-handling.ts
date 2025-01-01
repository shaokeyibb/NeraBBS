export type ErrorMessage = {
  code: number;
  message: string;
};

export type ErrorMap = {
  [key: string]: {
    [key: string]: string;
  };
};
