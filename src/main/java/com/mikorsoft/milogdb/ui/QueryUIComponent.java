package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.domain.MiLogColumns;

import java.util.List;

public record QueryUIComponent (Long qid,String title, List<MiLogColumns> columns) {}
