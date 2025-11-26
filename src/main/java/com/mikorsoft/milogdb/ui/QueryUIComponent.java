package com.mikorsoft.milogdb.ui;

import java.util.List;

public record QueryUIComponent (Long qid,String title, List<String> columns) {}
