package org.statesync.info;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SyncSessionInfo {
	String sessionToken;
	String externalSessionId;
}