package org.statesync.info;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StateSyncInfo {
	public List<String> userIds;
	public final List<SyncSessionInfo> sessions = new ArrayList<>();
	public final List<SyncAreaInfo> areas = new ArrayList<>();
}
