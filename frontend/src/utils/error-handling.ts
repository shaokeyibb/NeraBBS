import type { ErrorMap, ErrorMessage } from "../types/error-handling.ts";

export const errorMap: ErrorMap = {
  sign_in: {
    "401": "error.signIn.unauthorized",
  },
  sign_up: {
    "403": "error.signUp.forbidden",
  },
  default: {
    "400": "error.bad_request",
    "403": "error.forbidden",
    "404": "error.not_found",
    default: "error.unknown",
  },
};

export function getErrorMessage(
  error: ErrorMessage,
  scope: string = "default",
) {
  const errorMessages = errorMap[scope] || errorMap.default;
  return (
    errorMessages[error.code] ||
    errorMessages.default ||
    errorMap.default.default
  );
}