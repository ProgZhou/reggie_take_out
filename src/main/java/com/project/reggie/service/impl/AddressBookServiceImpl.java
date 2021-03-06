package com.project.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.reggie.entity.AddressBook;
import com.project.reggie.mapper.AddressBookMapper;
import com.project.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
