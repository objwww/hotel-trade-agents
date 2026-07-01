package com.hotel.admin.interfaces.response;

import java.util.List;

public record MonitorTraceResponse(
        String traceId,
        List<?> operationLogs,
        List<?> statusLogs
) {
}