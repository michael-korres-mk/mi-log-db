package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.domain.MiLogColumn;
import com.mikorsoft.milogdb.domain.MiLogFilter;

import java.util.List;

public record QueryUIComponent(Long qid, String title, List<MiLogColumn> columns,List<MiLogFilter> filters) {}
