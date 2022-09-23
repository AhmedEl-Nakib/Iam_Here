package com.nakib.iamhere.ui.homeNormal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nakib.iamhere.MainActivity
import com.nakib.iamhere.R
import com.nakib.iamhere.databinding.HomeNormalFragmentBinding
import com.nakib.iamhere.model.home.NormalHomeResponseModel
import com.nakib.iamhere.utils.CommonUtils
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList

class HomeNormalFragment : Fragment(), HomeNormalTableAdapter.DeleteItem {

    lateinit var binding: HomeNormalFragmentBinding
    private val viewModel: HomeNormalViewModel by inject()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var sharedPreferences: SharedPreferences
    var locationManager: LocationManager? = null
    var GpsStatus = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeNormalFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("iamhere", Context.MODE_PRIVATE)

        handleView()
        handleClick()
        handleObserver()
    }

    private fun handleObserver() {
        viewModel.homeData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.tableId.adapter = HomeNormalTableAdapter(it, this)
            } else {
                val list = ArrayList<NormalHomeResponseModel>()
                binding.tableId.adapter = HomeNormalTableAdapter(list, this)
                binding.tableId.adapter!!.notifyDataSetChanged()
            }
        })
        viewModel.deleteRecord.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.getHomeList(requireContext(), requireArguments().getString("UserId")!!)
                viewModel.deleteRecord.value = false
            }
        })
    }

    private fun handleClick() {

        binding.calendarBtnId.setOnClickListener {
            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    viewModel.date.value = "$year-${monthOfYear + 1}-$dayOfMonth"
                    binding.calendarBtnId.text = "Search by Calendar ${monthOfYear + 1}-$dayOfMonth"
                    viewModel.getHomeList(
                        requireContext(),
                        requireArguments().getString("UserId")!!
                    )
                },
                year,
                month,
                day
            )
//            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
//            Navigation.findNavController(it).navigate(R.id.action_homeNormalFragment_to_timeTableNormalFragment)
        }

        binding.allowLocationBtnId.setOnClickListener {
            if (!CheckGpsStatus(requireContext()))
                Toast.makeText(requireContext(), "You need to open GPS first !", Toast.LENGTH_SHORT)
                    .show()
            else {
                (activity as MainActivity?)!!.getStarted(requireArguments().getString("UserId")!!)
                binding.allowLocationBtnId.isEnabled = false
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("CurrentDate", CommonUtils.getCurrentDate())
                editor.apply()
                Toast.makeText(requireContext(), "Thanks for attendance", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleView() {
//        binding.tableId.adapter = HomeNormalTableAdapter()
        if (requireArguments().getString("ViewType").equals("Admin")) {
//            http://masry.live/app/json/Attendance.php?action=getAllAttendance for table
            binding.addNewBtnId.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_homeNormalFragment_to_doctorLocationFragment)
            }
            binding.addNewBtnId.text = "Search for members"
        } else {
//            http://masry.live/app/json/Attendance.php?action=GetUserAttendance
            binding.addNewBtnId.setOnClickListener {
                val bundle = bundleOf("UserId" to requireArguments().getString("UserId"))
                Navigation.findNavController(it)
                    .navigate(R.id.action_homeNormalFragment_to_addNewTimeFragment, bundle)
            }
            binding.addNewBtnId.text = "Add new"
            binding.drNameId.text = "Welcome Dr : ${requireArguments().getString("DrName")}"
            viewModel.date.value = CommonUtils.getCurrentDate()
            viewModel.getHomeList(requireContext(), requireArguments().getString("UserId")!!)

            if (sharedPreferences.getString("CurrentDate", "Def")
                    .equals(CommonUtils.getCurrentDate())
            ) {
                binding.allowLocationBtnId.isEnabled = false
            } else {

            }

//            if(!CheckGpsStatus(requireContext()))
//                Toast.makeText(requireContext(),"You need to open GPS to resume location updates",Toast.LENGTH_SHORT).show()

            binding.learnMoreId.setOnClickListener {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Attention !")
                    .setMessage("- By clicking this button you submit your attendance (Click once at start of every day before your first lecture time)\n- Don't forget to open GPS first\n- When this button activate for each day you can not add any more lectures of this day")
                    .setPositiveButton("Ok") { p0, p1 ->
                        p0.dismiss()
                    }.create().show()
            }

        }
        binding.settingId.setOnClickListener {
            val bundle = bundleOf("UserId" to requireArguments().getString("UserId"), "DrName" to requireArguments().getString("DrName"))
            Navigation.findNavController(it)
                .navigate(R.id.action_homeNormalFragment_to_timeTableNormalFragment, bundle)
        }

    }
//            Toast.makeText(requireContext(),sharedPreferences.getString("CurrentDate","Def"), Toast.LENGTH_SHORT).show()

//        }


    override fun deleteItem(item: NormalHomeResponseModel) {
        if (item.attDate == CommonUtils.getCurrentDate()) {
            Toast.makeText(
                requireContext(),
                "You cannot delete a record of today's date\nYou can select another date",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Confirmation !")
                .setMessage("Are you sure for deleting this record ?")
                .setPositiveButton("Yes") { p0, p1 ->
                    viewModel.deleteRecord(
                        requireContext(),
                        requireArguments().getString("UserId")!!,
                        item.recordId
                    )
                    p0.dismiss()
                }
                .setNegativeButton("No") { p0, p1 ->
                    p0.dismiss()
                }.create().show()
        }
    }

    private fun CheckGpsStatus(context: Context): Boolean {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        assert(locationManager != null)
        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return GpsStatus
    }
}