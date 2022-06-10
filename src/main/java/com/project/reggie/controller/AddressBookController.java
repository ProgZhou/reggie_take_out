package com.project.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.project.reggie.common.BaseContext;
import com.project.reggie.common.CommonResult;
import com.project.reggie.entity.AddressBook;
import com.project.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ProgZhou
 * @createTime 2022/06/08
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    /*
    * 新增用户收货地址
    * */
    @PostMapping
    public CommonResult<AddressBook> addAddress(@RequestBody AddressBook addressBook) {
        log.info("接收到的地址信息: {}", addressBook);
        //设置用户id
        addressBook.setUserId(BaseContext.getCurrentUserId());
        addressBookService.save(addressBook);

        return CommonResult.success(addressBook);
    }

    /*
    * 设置默认收获地址
    * */
    @PutMapping("/default")
    public CommonResult<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("address: {}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentUserId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //先将所有地址都不设置为默认地址
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return CommonResult.success(addressBook);

    }

    /*
    * 根据id查询地址信息
    * */
    @GetMapping("/{id}")
    public CommonResult<AddressBook> getAddress(@PathVariable("id") Long id) {
        log.info("待查询的id: {}", id);
        AddressBook addressBook = addressBookService.getById(id);
        if(null == addressBook) {
            return CommonResult.error("没有查询到地址");
        }
        return CommonResult.success(addressBook);
    }

    /*
    * 查询默认地址信息
    * */
    @GetMapping("/default")
    public CommonResult<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentUserId());
        wrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(wrapper);
        if(null == addressBook) {
            return CommonResult.error("还没有设置默认地址，请设置");
        }

        return CommonResult.success(addressBook);
    }

    /*
    * 查询某个用户的所有地址信息
    * */
    @GetMapping("/list")
    public CommonResult<List<AddressBook>> getList(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentUserId());
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        wrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBookList = addressBookService.list(wrapper);

        return CommonResult.success(addressBookList);
    }


    /*
    * 根据id删除某个用户的地址信息
    * */
    @DeleteMapping
    public CommonResult<String> deleteAddress(@RequestParam("ids") Long ids) {
        log.info("待删除的地址: {}", ids);
        addressBookService.removeById(ids);
        return CommonResult.success("删除成功");
    }

}
