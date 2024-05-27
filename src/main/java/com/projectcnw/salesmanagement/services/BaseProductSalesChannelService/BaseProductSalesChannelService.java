package com.projectcnw.salesmanagement.services.BaseProductSalesChannelService;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.SalesChannelDTO.ChannelStatusDTO;
import com.projectcnw.salesmanagement.dto.SalesChannelDTO.ChannelStatusResponseDTO;
import com.projectcnw.salesmanagement.dto.SalesChannelDTO.PublishProductDTO;
import com.projectcnw.salesmanagement.dto.SalesChannelDTO.PublishProductResponseDTO;
import com.projectcnw.salesmanagement.models.BaseProductSalesChannel;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.SalesChannel;
import com.projectcnw.salesmanagement.repositories.BaseProductSalesChannelRepository.BaseProductSalesChannelRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.BaseProductRepository;
import com.projectcnw.salesmanagement.repositories.SaleChannelRepository.SalesChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseProductSalesChannelService {

    private final BaseProductSalesChannelRepository baseProductSalesChannelRepository;

    private final BaseProductRepository baseProductRepository;

    private final SalesChannelRepository salesChannelRepository;

    @Transactional
    public ResponseEntity<ResponseObject> publishProduct(PublishProductDTO publishProductDTO) {
        BaseProduct product = baseProductRepository.findBaseProductByIdAndIsDeleted_False(publishProductDTO.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.ok(
                    ResponseObject.builder().
                            responseCode(404).
                            message("Product not found").
                            build()
            );
        }

        List<ChannelStatusResponseDTO> channelStatusResponseList = new ArrayList<>();

        for (ChannelStatusDTO statusDTO : publishProductDTO.getChannelStatus()) {
            SalesChannel salesChannel = salesChannelRepository.findById(Long.valueOf(statusDTO.getChannelId())).orElse(null);
            if (salesChannel == null) {
                return ResponseEntity.ok(
                        ResponseObject.builder().
                                responseCode(404).
                                message("Channel not found").
                                build()
                );
            }

            BaseProductSalesChannel baseProductSalesChannel = new BaseProductSalesChannel();
            baseProductSalesChannel.setBaseProduct(product);
            baseProductSalesChannel.setSalesChannel(salesChannel);
            baseProductSalesChannel.setActive(statusDTO.isActive());

            baseProductSalesChannelRepository.save(baseProductSalesChannel);

            ChannelStatusResponseDTO responseDTO = new ChannelStatusResponseDTO();
            responseDTO.setChannelId(salesChannel.getId());
            responseDTO.setChannelName(salesChannel.getName());
            responseDTO.setActive(statusDTO.isActive());

            channelStatusResponseList.add(responseDTO);
        }

        PublishProductResponseDTO responseDTO = new PublishProductResponseDTO();
        responseDTO.setProductId(Long.valueOf(product.getId()));
        responseDTO.setChannelStatus(channelStatusResponseList);

        return ResponseEntity.ok(
                ResponseObject.builder().
                        responseCode(200).
                        message("Publish product successfully").
                        data(responseDTO).
                        build()
        );

    }

    @Transactional
    public ResponseEntity<ResponseObject> updatePublishProduct (Integer productId, PublishProductDTO publishProductDTO) {
        BaseProduct product = baseProductRepository.findBaseProductByIdAndIsDeleted_False(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.ok(
                    ResponseObject.builder().
                            responseCode(404).
                            message("Product not found").
                            build()
            );
        }

//        List<BaseProductSalesChannel> productSalesChannels = baseProductSalesChannelRepository.findByBaseProduct_Id(product.getId());

        List<ChannelStatusResponseDTO> listChannelResponse = new ArrayList<>();
        for (ChannelStatusDTO statusDTO : publishProductDTO.getChannelStatus()) {
            SalesChannel salesChannel = salesChannelRepository.findById(Long.valueOf(statusDTO.getChannelId())).orElse(null);
            if (salesChannel == null) {
                return ResponseEntity.ok(
                        ResponseObject.builder().
                                responseCode(404).
                                message("Channel not found").
                                build()
                );
            }

            BaseProductSalesChannel baseProductSalesChannel = baseProductSalesChannelRepository.findByBaseProduct_IdAndSalesChannel_Id(product.getId(), salesChannel.getId());
            if (baseProductSalesChannel == null) {
                baseProductSalesChannel = new BaseProductSalesChannel();
                baseProductSalesChannel.setBaseProduct(product);
                baseProductSalesChannel.setSalesChannel(salesChannel);
            }

            baseProductSalesChannel.setActive(statusDTO.isActive());

            baseProductSalesChannelRepository.save(baseProductSalesChannel);

            listChannelResponse.add(ChannelStatusResponseDTO.builder()
                    .channelId(salesChannel.getId())
                    .channelName(salesChannel.getName())
                    .active(statusDTO.isActive())
                    .build());
        }

        PublishProductResponseDTO responseDTO = new PublishProductResponseDTO();
        responseDTO.setProductId(Long.valueOf(product.getId()));
        responseDTO.setChannelStatus(listChannelResponse);

        return ResponseEntity.ok(
                ResponseObject.builder().
                        responseCode(200).
                        message("Update publish product successfully").
                        data(responseDTO).
                        build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> getProductSalesChannels(Integer productId) {
        BaseProduct product = baseProductRepository.findBaseProductByIdAndIsDeleted_False(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.ok(
                    ResponseObject.builder().
                            responseCode(404).
                            message("Product not found").
                            build()
            );
        }

        List<BaseProductSalesChannel> productSalesChannels = baseProductSalesChannelRepository.findByBaseProduct_Id(productId);

        List<ChannelStatusResponseDTO> channelStatusResponseList = productSalesChannels.stream().map(productSalesChannel -> {
            ChannelStatusResponseDTO responseDTO = new ChannelStatusResponseDTO();
            responseDTO.setChannelId(productSalesChannel.getSalesChannel().getId());
            responseDTO.setChannelName(productSalesChannel.getSalesChannel().getName());
            responseDTO.setActive(productSalesChannel.isActive());

            return responseDTO;
        }).collect(Collectors.toList());

        PublishProductResponseDTO responseDTO = new PublishProductResponseDTO();
        responseDTO.setProductId(Long.valueOf(product.getId()));
        responseDTO.setChannelStatus(channelStatusResponseList);

        return ResponseEntity.ok(
                ResponseObject.builder().
                        responseCode(200).
                        message("Publish product successfully").
                        data(responseDTO).
                        build()
        );

    }

    @Transactional
    public void publishAllUnpublishedProducts() {
        List<BaseProduct> allProducts = baseProductRepository.findAll();

        List<BaseProduct> unpublishedProducts = allProducts.stream().filter(product ->
                baseProductSalesChannelRepository.findByBaseProduct_Id(product.getId()).isEmpty()
        ).toList();

        List<SalesChannel> allSalesChannels = salesChannelRepository.findAll();

        for (BaseProduct product : unpublishedProducts) {
            for (SalesChannel salesChannel : allSalesChannels) {
                BaseProductSalesChannel baseProductSalesChannel = new BaseProductSalesChannel();
                baseProductSalesChannel.setBaseProduct(product);
                baseProductSalesChannel.setSalesChannel(salesChannel);
                baseProductSalesChannel.setActive(true); // default active, can be modified

                baseProductSalesChannelRepository.save(baseProductSalesChannel);
            }
        }
    }
}
