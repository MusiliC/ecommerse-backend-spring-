package com.ecommerce.shop.service;

import com.ecommerce.shop.dtos.AddressDto;
import com.ecommerce.shop.dtos.AddressResponse;
import com.ecommerce.shop.exceptions.ResourceNotFoundException;
import com.ecommerce.shop.models.Address;
import com.ecommerce.shop.models.User;
import com.ecommerce.shop.repository.AddressRepository;
import com.ecommerce.shop.repository.UserRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressServiceI {

    private final ModelMapper modelMapper;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;


    @Override
    public AddressDto addAddress(AddressDto addressDto, User user) {

        Address address = modelMapper.map(addressDto, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDto.class);

    }

    @Override
    public AddressResponse getAddresses() {
        List<AddressDto> addressDtos = addressRepository.findAll()
                .stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();

        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(addressDtos);
        return addressResponse;

    }

    @Override
    public AddressDto getSIngleAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public AddressResponse getAddressesByUser(User user) {
        List<Address> userAddresses = user.getAddresses();

        List<AddressDto> addressDtos = userAddresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();

        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(addressDtos);
        return addressResponse;
    }

    @Override
    public AddressDto updateAddress(Long addressId, AddressDto addressDto) {
        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressFromDb.setCity(addressDto.getCity());
        addressFromDb.setCountry(addressDto.getCountry());
        addressFromDb.setBuilding(addressDto.getBuilding());
        addressFromDb.setStreet(addressDto.getStreet());

        Address updatedAddress = addressRepository.save(addressFromDb);

        User user = addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDto.class);

    }

    @Override
    public String deleteAddress(Long addressId) {

        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));


        User user = addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));

        userRepository.save(user);

        addressRepository.delete(addressFromDb);

        return "Address Deleted Successfully";
    }

}
