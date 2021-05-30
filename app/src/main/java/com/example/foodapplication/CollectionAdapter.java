package com.example.foodapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.CollectionModel;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    List<CollectionModel> my_list;
    Context context;

    public CollectionAdapter(List<CollectionModel> my_list, Context context){
        this.my_list = my_list;
        this.context = context;
    }

    @NonNull
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_collection_linear_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.ViewHolder holder, int position) {
        CollectionModel collectionModel = my_list.get(position);
        holder.title.setText(collectionModel.getItem_name());
        holder.linearIcon.setImageDrawable(context.getResources().getDrawable(collectionModel.getImage()));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDescription;
                Intent intent = new Intent(context, Item_Collection.class);
                intent.putExtra("image", collectionModel.getImage());
                intent.putExtra("name", collectionModel.getItem_name());
                switch (position) {
                    case 0:
                        sDescription = "[MỞ CỬA RA ĐÓN DEAL BÁNH MÌ ÙA VỀ]\n" +
                                "Ăn sáng chất lượng mà không tốn kém với deal \"thơm bơ\" từ Bánh mì Việt Nam. \n" +
                                "✅Nhập mã BMMN30 giảm ngay 30k khi đặt món tại Now với hóa đơn tối thiểu 50k\n" +
                                "✅Áp dụng từ 1/6 - 15/7, số lượng mã có hạn\n";
                        intent.putExtra("description", sDescription);
                        break;
                    case 1:
                        sDescription = "[7 NGÀY REVIEW – TIỀN TRIỆU VỀ TÚI]\n" +
                                "️\uD83C\uDF89️\uD83C\uDF89 Loa Loa Loa!! Cả nhà ơi, cuộc đua review NowFood đã trở lại rồi đây. Mọi người đã sẵn sàng “vặn ga” gia nhập đường đua chưa? Lần này phần thưởng cực kì hấp dẫn đảm bảo ai cũng mê nhe: đó chính là 03 triệu đồng tiền mặt! Xin nhắc lại 03 TRIỆU ĐỒNG tiền mặt chuyển khoản ting ting cho tay đua Giải Nhất kì này cùng các giải may mắn dành cho các tay đua được tiên nữ Winx gọi tên.\n" +
                                "Chờ gì nữa mà không tham gia đường đua ngay nè:\n" +
                                "️\uD83D\uDCB0\uD83D\uDCB0 PHẦN THƯỞNG\n" +
                                "- Giải nhất: 03 triệu đồng dành cho tay đua có số review hợp lệ nhiều nhất từ 0h ngày 29.4 đến 12h trưa ngày 05.05.2021\n" +
                                "- Giải may mắn: 05 voucher 50k dành cho 05 tay đua may mắn có ít nhất 1 review hợp lệ và có số may mắn trùng hoặc gần đúng nhất với 05 lần quay số tại random.org.\n" +
                                "\uD83D\uDCA1\uD83D\uDCA1 THAM GIA NGAY BẰNG CÁCH: \n" +
                                "Bước 1: Đặt món tại các quán trên ứng dụng Now (hoặc ứng dụng Now trên Shopee/AirPay)\n" +
                                "Bước 2: Viết review cho shipper và nhà hàng trên ứng dụng Now sau khi nhận món kèm ít nhất 01 ảnh thật chụp món\n" +
                                "Bước 3: Điền username Now và số may mắn bạn chọn vào link này: http://bit.ly/7ngayreviewNow.\n" +
                                "⚠️ Lưu ý: \n" +
                                "Chỉ đua review trên 01 tài khoản mà bạn đã đăng kí thôi nhé!\n" +
                                "Ban Tổ Chức sẽ chỉ xét trao giải cho các bạn có điền thông tin tại đường link đăng ký.\n" +
                                "Chi tiết thể lệ: https://shopee.vn/events3/code/1915149433/\n" +
                                "\uD83C\uDFC6️\uD83C\uDFC6 CÔNG BỐ KẾT QUẢ: Kết quả sẽ được công bố vào ngày 6.5.2021 tại fanpage Now.vn. Các bạn nhớ theo dõi nhé!";
                        intent.putExtra("description", sDescription);
                        break;
                    case 2:
                        sDescription = "[SĂN MÓN BẠN YÊU - TIÊU CODE THOẢI MÁI]\n" +
                                "Thả thính dàn code to bự, xịn mịn chỉ có duy nhất với số lượng có hạn cho bạn tha hồ săn món yêu thích. \n" +
                                "Chờ gì nữa mà không bỏ giỏ hàng ngay nè!";
                        intent.putExtra("description", sDescription);
                        intent.putExtra("description", sDescription);
                        break;
                    case 3:
                        sDescription = "[CUỐI TUẦN FREESHIP - BÍ KÍP ĂN CẢ THẾ GIỚI]\n" +
                                "Nhanh tay lưu gấp các quán xịn mịn đang được giảm giá sập sàn tại Đà Nẵng nè mọi người ơi: cao lầu, bánh mì nướng, ăn vặt, trà sữa, ... đều có nha. Ngoài ra, nhớ nhập mã FSDN để tha hồ thưởng thức món ngon mà không lo phí ship nhé cả nhà!\n" +
                                "✅ Áp dụng từ 13/6 - 15/7";
                        intent.putExtra("description", sDescription);
                        break;
                    case 4:
                        sDescription = "Đẩy đơn ngày lễ chẳng lo phí ship khi ĐỒNG GIÁ 15K PHÍ SHIP HÀNG cho chủ shop\n" +
                                "Ngày xưa: \"Lễ phí ship cao lắm\"\n" +
                                "Ngày nay: ĐỒNG GIÁ SHIP HÀNG 15K cho các đơn hàng dưới 5km cho các chủ shop thả ga đẩy đơn trong ngày lễ luôn nha. Đừng quên nhập mã NOWSHIP15K để được áp dụng ưu đãi khi sử dụng NowShip.\n" +
                                "Tham gia http://bit.ly/HoiChuShopgiaohangNowShip để tham gia Đường Đua Triệu Phú mùa 2 với những phần quà siêu khủng như Apple Watch SE, Airpods Pro,..... và có cơ hội nhận gói VOUCHER GIẢM PHÍ SHIP XUỐNG 0Đ cực chất.";
                        intent.putExtra("description", sDescription);
                        break;
                    case 5:
                        sDescription = "DEAL NỬA GIÁ DÀNH RIÊNG QUÁN GẦN NHÀ - ĐẶT NGAY]\n" +
                                "Đây đích thị là chiếc deal sinh ra dành cho mình rồi nè! Quán sát bên nhưng vẫn lười nhấc mông lên đi ăn \uD83D\uDE42 Chờ gì nữa mà không vợt ngay chiếc deal này:\n" +
                                "✅ Nhập mã DEALGANNHA giảm 50% tối đa 20k\n" +
                                "Nhanh tay bật app chốt đơn nào các đồng ăn ơi!";
                        intent.putExtra("description", sDescription);
                        break;
                    case 6:
                        sDescription =  "Ngay sau sự kiện Đại Tiệc Thương Hiệu 4.4, NowFood chính thức trở lại với chương trình mang chủ đề \" Deal xịn - không phải nhịn\".\n" +
                                "\n" +
                                "Đây chính là loạt ưu đãi đặc biệt tiếp nối ngay giữa năm giúp các tín đồ mê ẩm thực không chỉ thưởng thức được loạt món siêu ngon, siêu hấp dẫn mà còn tiết kiệm được kha khá với những ưu đãi giảm cực sâu khi càng mua càng được giảm với những bộ deal \"xịn xò\".\n" +
                                "\n" +
                                "Ngay từ bây giờ người dùng đã có thể bắt ngay cơ hội săn hàng nghìn deal xịn giảm tới 70K khi đặt món chỉ có trên ứng dụng Now và Shopee.\n" +
                                "\n";
                        intent.putExtra("description", sDescription);
                        break;
                    case 7:
                        sDescription = "[FREESHIP QUÁN NGON - CÒN CHỜ CHI NỮA - NOW NGAY!]\n" +
                                "Nowfood món Việt mênh mông , hóng mã freeship tha hồ ăn ngon! Lên Now chốt đơn săn mã xịn để tha hồ ăn uống không lo phí ship nhe bà con!\n" +
                                "✅Nhập mã FSVIETNAM giảm 15k cho đơn từ 30k\n" +
                                "✅Áp dụng cho mọi Quán Đối Tác\n" +
                                "✅Số lượng mã có hạn";
                        intent.putExtra("description", sDescription);
                        break;
                    case 8:
                        sDescription = "Khởi động cho Tiệc chào hè 5.5, để giúp \"thượng đế\" an tâm ngồi tại nhà hoặc nơi làm việc thưởng thức các món ngon mà không phải lo lắng về những cơn mưa rào bất chợt hay cái nắng gắt chói chang đầu hè. NowFood đã tung ra các Bộ sưu tập rất hấp dẫn như đồng giá các món ngon chỉ còn 5K, Combo chỉ 55K, các deal đếm ngược về 1Đ... giúp người dùng tha hồ tận hưởng bữa tiệc xịn đúng chất hè.";
                        intent.putExtra("description", sDescription);
                        break;
                    default:
                        sDescription = "[DEAL MÊ ĐẮM - SẮM NGẤT NGÂY - DUY NHẤT TRONG THÁNG 6]\n" +
                        "Chỉ cần ở nhà mở app Now vào mục NowFresh là có ngay các mặt hàng từ thực phẩm tươi như thịt, hải sản, rau củ quả, sữa...với nhiều ưu đãi hấp dẫn lên đến 50% từ cửa hàng nha.\n" +
                                "Chưa hết đâu NowFresh còn có dàn code siêu yêu thương cho mọi người để thêm vào giỏ hàng đi chợ đây!";
                        intent.putExtra("description", sDescription);
                        break;

                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView linearIcon;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView_NameVoucher);
            linearIcon = itemView.findViewById(R.id.imageView_Collection);
            linearLayout = itemView.findViewById(R.id.Collection_LinearLayout);
        }
    }
}
