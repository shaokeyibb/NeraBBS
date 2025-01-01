import type { ErrorMessage } from "../types/error-handling.ts";
import { snackbar } from "mdui";
import { getErrorMessage } from "../utils/error-handling.ts";
import { useI18n } from "vue-i18n";

export default function useErrorHandling() {
  const { t } = useI18n();

  const handle = (error: ErrorMessage, scope: string = "default") => {
    snackbar({
      message: t(getErrorMessage(error, scope), {
        code: error.code,
        message: error.message,
      }),
      closeable: true,
      queue: "error",
      autoCloseDelay: 3000,
    });
  };

  return {
    handle,
  };
}
